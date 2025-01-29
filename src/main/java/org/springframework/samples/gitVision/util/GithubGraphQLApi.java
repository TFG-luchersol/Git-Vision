package org.springframework.samples.gitvision.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.contributions.model.CommitContribution;
import org.springframework.samples.gitvision.util.graphQL.models.GraphQLCommitResponse;
import org.springframework.web.client.RestTemplate;

public class GithubGraphQLApi {

    private RestTemplate restTemplate;
    private String token;

    private GithubGraphQLApi(String token) {
        this.token = token;
        this.restTemplate = new RestTemplate();
    }

    public static GithubGraphQLApi connect(String token) {
        return new GithubGraphQLApi(token);
    }

    private <T> T requestGithubGraphQL(String query, Map<String, Object> vars, Class<T> clazz) {
        // Crear un objeto de variables para pasar a la consulta
        JSONObject variables = new JSONObject();
        vars.entrySet().forEach(entry -> variables.put(entry.getKey(), entry.getValue()));

        // Crear el payload JSON con las variables
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query);
        jsonObject.put("variables", variables);

        // Crear la solicitud HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + this.token);

        HttpEntity<String> entity = new HttpEntity<>(jsonObject.toString(), headers);

        ResponseEntity<T> response = this.restTemplate.exchange(
                "https://api.github.com/graphql",
                HttpMethod.POST,
                entity,
                clazz);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new IllegalAccessError("Petición GraphQL fallada: " + response.getStatusCode());
        }
    }

    public List<CommitContribution> getContributionsBetweenDates(String repositoryName, Date startDate, Date endDate) throws IOException {
        String[] owner_repo = repositoryName.split("/");
        String query = readGraphQLQuery("getContributionsBetweenDates.graphql");
        Map<String, Object> vars = new HashMap<>();
        vars.put("owner", owner_repo[0]);
        vars.put("repo", owner_repo[1]);
        if(startDate != null) {
            String startDateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(startDate);
            vars.put("startDate", startDateStr);
        }
        if(endDate != null) {
            String endDateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(endDate);
            vars.put("endDate", endDateStr);
        }
        vars.put("cursor", null);
        List<CommitContribution> allContributions = new ArrayList<>();
        boolean hasNextPage = true;
        while(hasNextPage) {
            GraphQLCommitResponse response = this.requestGithubGraphQL(query, vars, GraphQLCommitResponse.class);
            
            List<CommitContribution> contributions = response.getData()
                .getRepository()
                .getDefaultBranchRef()
                .getTarget()
                .getHistory()
                .getEdges()
                .stream()
                .map(edge -> {
                    CommitContribution contribution = new CommitContribution();
                    contribution.setCommittedDate(edge.getNode().getCommittedDate());
                    contribution.setAdditions(edge.getNode().getAdditions());
                    contribution.setDeletions(edge.getNode().getDeletions());
                    contribution.setAuthorName(edge.getNode().getAuthor().getName());
                    contribution.setAuthorEmail(edge.getNode().getAuthor().getEmail());
                    return contribution;
                })
                .collect(Collectors.toList());

            // Añadir los resultados actuales a la lista total
            allContributions.addAll(contributions);

            // Revisar si hay más páginas
            var pageInfo = response.getData().getRepository().getDefaultBranchRef().getTarget().getHistory().getPageInfo();
            hasNextPage = pageInfo.isHasNextPage();
            String cursor = pageInfo.getEndCursor();

            // Actualizar el cursor para la siguiente solicitud
            vars.put("cursor", cursor);
        }
        return allContributions;
    }

    private static String readGraphQLQuery(String filePath) throws IOException {
        String relativePath = "./src/main/java/org/springframework/samples/gitvision/util/graphQL";
        Path path = Paths.get(relativePath, filePath);
        return Files.readString(path);
    }
}
