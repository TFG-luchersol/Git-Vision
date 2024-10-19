import React from "react";
import { Route, Routes } from "react-router-dom";
import { ErrorBoundary } from "react-error-boundary";

import Login from "./auth/login"
import Register from "./auth/register";
import SwaggerDocs from "./public/swagger";
import AppNavbar from "./appNavbar";
import Home from "./home";
import Details from "./details";
import Repositories from "./repositories";
import Repository from "./repositories/repository";
import WorkspaceDownload from "./extraction/workspaceDownload";
import RepositoryDownload from "./extraction/repositoryDownload";
import RepositoryWorkspaceLinker from "./extraction/repositoryWorkspaceLinker";
import Issues from './repositories/repository/issues';
import Issue from './repositories/repository/issues';
import Commits from './repositories/repository/commits';
import Commit from './repositories/repository/commits/commit';


function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  )
}

function App() {
  const jwt = null; // tokenService.getLocalAccessToken();

  let userRoutes = <></>;
  let publicRoutes = <></>;

  userRoutes = (
      <>
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/details" element={<Details />}/>
        <Route path="/repositories" element={<Repositories/>}/>
        <Route path="/repository/:owner/:repo" element={<Repository/>}/>
        <Route path="/repository/:owner/:repo/commits" element={<Commits/>}/>
        <Route path="/repository/:owner/:repo/commits/:sha" element={<Commit/>}/>
        <Route path="/repository/:owner/:repo/issues" element={<Issues/>}/>
        <Route path="/repository/:owner/:repo/issues/:issueNumber" element={<Issue/>}/>
        <Route path="/workspace/download" element={<WorkspaceDownload/>}/>
        <Route path="/repository/download" element={<RepositoryDownload/>}/>
        <Route path="/repository/workspace/linker" element={<RepositoryWorkspaceLinker/>}/>
      </>
    )

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback} >
        <AppNavbar />
        <Routes>
          <Route path="/" exact={true} element={<Home />} />
          <Route path="/swagger" element={<SwaggerDocs />} />
          {publicRoutes}
          {userRoutes}
        </Routes>
      </ErrorBoundary>
    </div>
  );
}

export default App;
