import React from "react";
import { Route, Routes } from "react-router-dom";
import { ErrorBoundary } from "react-error-boundary";

import Login from "./login";
import Register from "./register";
import SwaggerDocs from "./public/swagger";
import AppNavbar from "./appNavbar/AppNavbar.js";
import Home from "./home/index.js";
import Details from "./details/index.js";
import Repositories from "./repositories/index.js";
import Repository from "./repositories/repository/index.js";

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
  // console.log(tokenService.getUser())
  userRoutes = (
      <>
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/details" element={<Details />}/>
        <Route path="/repositories" element={<Repositories/>}/>
        <Route path="/repository/1" element={<Repository/>}/>
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
