package org.springframework.samples.gitvision.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.gitvision.contributions.model.Contribution;
import org.springframework.samples.gitvision.util.graphQL.models.GraphQLContributionResponse;
import org.springframework.web.client.RestTemplate;

public class GithubGraphQLApi {

    private RestTemplate restTemplate;
    private String token;

    private static Function<Date, String> formatDate = date -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(date);


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

    public List<Contribution> getContributionsBetweenDates(String repositoryName, String filePath, Date startDate, Date endDate) throws IOException {
        String[] owner_repo = repositoryName.split("/");
        String query = readGraphQLQuery("getContributionsBetweenDates.graphql");
        Map<String, Object> vars = new HashMap<>();
        vars.put("owner", owner_repo[0]);
        vars.put("repo", owner_repo[1]);
        if(filePath != null) vars.put("filePath", filePath);
        if(startDate != null) vars.put("startDate", formatDate.apply(startDate));
        if(endDate != null) vars.put("endDate", formatDate.apply(endDate));
        vars.put("cursor", null);
        List<Contribution> allContributions = new ArrayList<>();
        boolean hasNextPage = true;
        while(hasNextPage) {
            GraphQLContributionResponse response = this.requestGithubGraphQL(query, vars, GraphQLContributionResponse.class);
            
            List<Contribution> contributions = response.getData()
                .getRepository()
                .getDefaultBranchRef()
                .getTarget()
                .getHistory()
                .getEdges()
                .stream()
                .map(edge -> {
                    Contribution contribution = new Contribution();
                    contribution.setCommittedDate(edge.getNode().getCommittedDate());
                    contribution.setAdditions(edge.getNode().getAdditions());
                    contribution.setDeletions(edge.getNode().getDeletions());
                    contribution.setAuthorName(edge.getNode().getAuthor().getName());
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
