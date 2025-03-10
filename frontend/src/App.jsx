import React from "react";
import { ErrorBoundary } from "react-error-boundary";
import { Route, Routes } from "react-router-dom";

import "@css";
import "@css/global.css";
import AppNavbar from "@screens/appNavbar";
import Login from "@screens/auth/login";
import Register from "@screens/auth/register";
import Details from "@screens/details";
import RepositoryDownload from "@screens/extraction/repositoryDownload";
import RepositoryWorkspaceLinker from "@screens/extraction/repositoryWorkspaceLinker";
import WorkspaceDownload from "@screens/extraction/workspaceDownload";
import Home from "@screens/home";
import SwaggerDocs from "@screens/public/swagger";
import Repositories from "@screens/repositories";
import Repository from "@screens/repositories/repository";
import Commits from "@screens/repositories/repository/commits";
import Commit from "@screens/repositories/repository/commits/commit";
import Contributors from "@screens/repositories/repository/contributors";
import File from "@screens/repositories/repository/file";
import Folder from "@screens/repositories/repository/folder";
import Issues from "@screens/repositories/repository/issues";
import Issue from "@screens/repositories/repository/issues/issue";
import RepositoryDetails from "@screens/repositories/repository/repositoryDetails";
import tokenService from "@services/token.service";

function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  );
}

function App() {
  const jwt = tokenService.getLocalAccessToken();

  let userRoutes = <></>;
  let publicRoutes = <></>;

  userRoutes = (
    <>
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/details" element={<Details />} />
      <Route path="/repositories" element={<Repositories />} />
      <Route path="/repository/:owner/:repo" element={<Repository />} />
      <Route path="/repository/:owner/:repo/blob/*" element={<File />} />
      <Route path="/repository/:owner/:repo/tree/*" element={<Folder />} />
      <Route
        path="/repository/:owner/:repo/details"
        element={<RepositoryDetails />}
      />
      <Route
        path="/repository/:owner/:repo/contributors"
        element={<Contributors />}
      />
      <Route path="/repository/:owner/:repo/commits" element={<Commits />} />
      <Route
        path="/repository/:owner/:repo/commits/:sha"
        element={<Commit />}
      />
      <Route path="/repository/:owner/:repo/issues" element={<Issues />} />
      <Route
        path="/repository/:owner/:repo/issues/:issueNumber"
        element={<Issue />}
      />
      <Route path="/workspace/download" element={<WorkspaceDownload />} />
      <Route path="/repository/download" element={<RepositoryDownload />} />
      <Route
        path="/repository/workspace/linker"
        element={<RepositoryWorkspaceLinker />}
      />
    </>
  );

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback}>
        <AppNavbar />
        <Routes>
          <Route path="/" exact={true} element={<Home />} />
          <Route path="/swagger" element={<SwaggerDocs />} />
          {publicRoutes}
          {jwt && userRoutes}
        </Routes>
      </ErrorBoundary>
    </div>
  );
}

export default App;
