import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./index.css";
import App from "./App.jsx";
import DoctorsListPage from "./DoctorsListPage.jsx";
import DoctorsLogin from "./DoctorsLogin.jsx";
import RegisterDoctor from "./RegisterDoctor.jsx";

const router = createBrowserRouter([
  { path: "/", element: <App /> },
  { path: "/doctors", element: <DoctorsListPage /> },
  { path: "/login", element: <DoctorsLogin /> },
  { path: "/register-doctor", element: <RegisterDoctor /> },
]);

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>
);
