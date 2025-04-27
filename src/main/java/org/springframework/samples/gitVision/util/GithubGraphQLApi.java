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
import org.springframework.samples.gitvision.change.model.Change;
import org.springframework.samples.gitvision.contributions.model.Contribution;
import org.springframework.samples.gitvision.file.model.ChangesInFile;
import org.springframework.samples.gitvision.util.graphQL.models.GraphQLChangesFiles;
import org.springframework.samples.gitvision.util.graphQL.models.GraphQLContributionResponse;
import org.springframework.samples.gitvision.util.graphQL.models.GraphQLContributionsByIssueNumber;
import org.springframework.samples.gitvision.util.graphQL.models.GraphQLTotalChangesInFile;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

public class GithubGraphQLApi {

    private RestTemplate restTemplate;
    private String token;

    private static Function<Date, String> formatDate = date -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .format(date);

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

    public List<Contribution> getContributionsBetweenDates(String repositoryName, String filePath, Date startDate,
            Date endDate) throws IOException {
        String[] owner_repo = repositoryName.split("/");
        String query = readGraphQLQuery("getContributionsBetweenDates.graphql");
        Map<String, Object> vars = new HashMap<>();
        vars.put("owner", owner_repo[0]);
        vars.put("repo", owner_repo[1]);
        if (filePath != null)
            vars.put("filePath", filePath);
        if (startDate != null)
            vars.put("startDate", formatDate.apply(startDate));
        if (endDate != null)
            vars.put("endDate", formatDate.apply(endDate));
        vars.put("cursor", null);
        List<Contribution> allContributions = new ArrayList<>();
        boolean hasNextPage = true;
        while (hasNextPage) {
            GraphQLContributionResponse response = this.requestGithubGraphQL(query, vars,
                    GraphQLContributionResponse.class);

            List<Contribution> contributions = response.getEdges()
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
            var pageInfo = response.getData().getRepository().getDefaultBranchRef().getTarget().getHistory()
                    .getPageInfo();
            hasNextPage = pageInfo.isHasNextPage();
            String cursor = pageInfo.getEndCursor();

            // Actualizar el cursor para la siguiente solicitud
            vars.put("cursor", cursor);
        }
        return allContributions;
    }

    public List<Contribution> getContributionsByIssueNumber(String repositoryName, Long issueNumber) throws IOException {
        String[] owner_repo = repositoryName.split("/");
        String query = readGraphQLQuery("getContributionsByIssueNumber.graphql");
        Map<String, Object> vars = new HashMap<>();
        vars.put("owner", owner_repo[0]);
        vars.put("repo", owner_repo[1]);
        vars.put("issueNumber", issueNumber);
        vars.put("cursor", null);
        List<Contribution> allContributions = new ArrayList<>();
        boolean hasNextPage = true;
        while (hasNextPage) {
            GraphQLContributionsByIssueNumber response = this.requestGithubGraphQL(query, vars,GraphQLContributionsByIssueNumber.class);

            if(response.hasErrors()) {
                throw new RuntimeException(response.getErrorMessage());
            }
            List<Contribution> contributions = response.getNodes()
                    .stream()
                    .map(node -> {
                        Contribution contribution = new Contribution();
                        contribution.setAdditions(node.getAdditions());
                        contribution.setDeletions(node.getDeletions());
                        contribution.setAuthorName(node.getAuthorName());
                        return contribution;
                    })
                    .collect(Collectors.toList());
            allContributions.addAll(contributions);
            hasNextPage = response.hasNextPage();
            String cursor = response.getEndCursor();

            vars.put("cursor", cursor);
        }
        return allContributions;
    }

    public List<ChangesInFile> getChangesInFilesByUser(String repositoryName, String authorName) throws IOException {
        // Separar owner y repo
        String[] ownerRepo = repositoryName.split("/");
        String query = readGraphQLQuery("getChangesInFilesByUser.graphql");

        // Inicializar variables para la paginación
        Map<String, Object> vars = new HashMap<>();
        vars.put("owner", ownerRepo[0]);
        vars.put("repo", ownerRepo[1]);
        vars.put("cursor", null);
        vars.put("authorName", authorName);
        boolean hasNextPage = true;
        Map<String, Change> fileChangesMap = new HashMap<>();

        while (hasNextPage) {
            // Realizar la consulta a GitHub GraphQL
            var response = this.requestGithubGraphQL(query, vars,
            JsonNode.class);

            // Procesar cada archivo modificado en cada commit
            // response.getNodes().forEach(commit -> {
            //     commit.getFiles().getEdges().stream().map(g -> g.getNode()).forEach(file -> {
            //         String filePath = file.getPath();
            //         int additions = file.getAdditions();
            //         int deletions = file.getDeletions();

            //         fileChangesMap
            //                 .computeIfAbsent(filePath, k -> new Change())
            //                 .addChanges(additions, deletions);
            //     });

            // });

            // // Obtener información de paginación
            // var pageInfo = response.getPageInfo();
            // hasNextPage = pageInfo.isHasNextPage();
            // vars.put("cursor", pageInfo.getEndCursor());
            int x = 0;
            break;
        }

        // Convertimos los datos en una lista de ChangesInFile
        return fileChangesMap.entrySet()
                .stream()
                .map(entry -> ChangesInFile.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public ChangesInFile getChangesByUserInFile(String repositoryName, String filePath) throws IOException {
        String[] owner_repo = repositoryName.split("/");
        String query = readGraphQLQuery("getChangesFiles.graphql");
        Map<String, Object> vars = new HashMap<>();
        vars.put("owner", owner_repo[0]);
        vars.put("repo", owner_repo[1]);
        vars.put("filePath", filePath);
        vars.put("cursor", null);
        boolean hasNextPage = true;
        Change change = new Change();
        while (hasNextPage) {
            GraphQLChangesFiles response = this.requestGithubGraphQL(query, vars, GraphQLChangesFiles.class);

            response.getNodes().forEach(node -> change.addChanges(node.getAdditions(), node.getDeletions()));

            // Revisar si hay más páginas
            var pageInfo = response.getPageInfo();
            hasNextPage = pageInfo.isHasNextPage();
            String cursor = pageInfo.getEndCursor();

            // Actualizar el cursor para la siguiente solicitud
            vars.put("cursor", cursor);
        }
        return ChangesInFile.of(filePath, change);
    }

    public Change getTotalChangesInFile(String repositoryName, String filePath) throws IOException {
        String[] owner_repo = repositoryName.split("/");
        String query = readGraphQLQuery("getTotalChangesInFile.graphql");
        Map<String, Object> vars = new HashMap<>();
        vars.put("owner", owner_repo[0]);
        vars.put("repo", owner_repo[1]);
        vars.put("filePath", filePath);
        Change change = new Change();
        GraphQLTotalChangesInFile response = this.requestGithubGraphQL(query, vars, GraphQLTotalChangesInFile.class);

        response.getCommits()
                .forEach(commit -> change.merge(Change.of(commit.getAdditions(), commit.getDeletions())));

        return change;
    }




    private static String readGraphQLQuery(String filePath) throws IOException {
        String relativePath = "./src/main/java/org/springframework/samples/gitvision/util/graphQL";
        Path path = Paths.get(relativePath, filePath);
        return Files.readString(path);
    }
}
