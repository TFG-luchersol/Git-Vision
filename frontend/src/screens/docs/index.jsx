import SideBar from "@components/SideBar";
import "@css/docs";
import React, { useEffect, useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import remarkSlug from "remark-slug";

export default function Documentation() {
  const [markdown, setMarkdown] = useState("");
  const [indexMarkdown, setIndexMarkdown] = useState("");
  const [isSidebarOpen, setIsSidebarOpen] = useState(true); // Para controlar el estado del sidebar

  function generarIndice(markdownText) {
    const encabezados = markdownText.match(/^(#{1,6})\s+(.*)/gm);

    if (!encabezados) return "";

    let indice = [];

    function generarId(texto) {
      return texto
        .toLowerCase()
        .replace(/[^a-z0-9áéíóúüñ\s-]/g, "") // Permitir tildes, ñ y caracteres especiales
        .replace(/\s+/g, "-") // Reemplazar espacios por guiones
        .replace(/^-+|-+$/g, ""); // Eliminar guiones al principio o final
    }

    encabezados.forEach((encabezado) => {
      const match = encabezado.match(/^(#{1,6})\s+(.*)/);
      if (match) {
        const nivel = match[1].length;
        const texto = match[2].trim();
        const id = generarId(texto);
        indice.push({ nivel, texto, id });
      }
    });

    let indiceFormateado = indice.map(({ nivel, texto, id }) => {
      const indentacion = "  ".repeat(nivel - 1);
      return `${indentacion}- [${texto}](#${encodeURIComponent(id)})`;
    });

    return indiceFormateado.join("\n");
  }

  useEffect(() => {
    fetch("/docs/index.md")
      .then((response) => response.text())
      .then((content) => {
        setMarkdown(content);
        setIndexMarkdown(generarIndice(content));
      })
      .catch((error) => alert(error));
  }, []);

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  const imageSize = {
    "Register": {width: "600px"},
    "Login": {width: "600px"},
    "Download_Repository": {width: "600px"},
    "Download_Workspace": {width: "600px"},
    "Linker_Repository_And_Workspace": {width: "600px"},
    "Workspace_With_Relation": {width: "600px"},
  }

  return (
    <div className="items-column">
      <SideBar
        indexMarkdown={indexMarkdown}
        isOpen={isSidebarOpen}
        toggleSidebar={toggleSidebar}
      />
      <div
        className={`content-docs ${isSidebarOpen ? "content-docs-open" : ""}`}
      >
        <ReactMarkdown
            key={markdown}
          children={markdown}
          remarkPlugins={[remarkSlug, remarkGfm]}
          components={{
            img: ({ node, ...props }) => {
              const width = imageSize[props.alt]?.width ?? "70%"
              const height = imageSize[props.alt]?.height ?? "auto"
              const fixImagePath = (path) => "/docs" + path.slice(path.startsWith(".") ? 1 : 0);
              return (
                <img
                  {...props}
                  style={{
                    width,
                    height,
                    margin: "0 auto",
                    display: "block",
                  }}
                  src={fixImagePath(props.src)}
                  alt={props.alt}
                />
              );
            },

            blockquote: ({ node, ...props }) => {
                console.log(props.children)
                const text = props.children?.[1]?.props?.children?.[0] || "";
                // Determinar el estilo basado en el contenido inicial
                let style = {
                  backgroundColor: "#f8f9fa", // gris clarito por defecto
                  borderLeft: "4px solid #dee2e6",
                  color: "#212529",
                  padding: "1em",
                  margin: "1em 0",
                  borderRadius: "8px",
                  fontStyle: "normal",
                };
              
                if (typeof text === "string") {
                  if (text.includes("⚠️")) {
                    style = {
                      backgroundColor: "#FFF3CD", // amarillo claro
                      borderLeft: "4px solid #FFEEBA",
                      color: "#856404",
                      padding: "1em",
                      margin: "1em 0",
                      borderRadius: "8px",
                      fontStyle: "normal",
                    };
                  } else if (text.includes("ℹ️")) {
                    style = {
                      backgroundColor: "#D1ECF1", // azul claro
                      borderLeft: "4px solid #BEE5EB",
                      color: "#0C5460",
                      padding: "1em",
                      margin: "1em 0",
                      borderRadius: "8px",
                      fontStyle: "normal",
                    };
                  }
                }
              
                return (
                  <blockquote style={style} {...props}>
                    {props.children}
                  </blockquote>
                );
              },
          }}
        />
      </div>
    </div>
  );
}
