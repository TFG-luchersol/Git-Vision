package org.springframework.samples.gitvision.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.samples.gitvision.file.File;

public class LanguageDetector {

    private static final Map<String, String> extensionToLanguageMap = new HashMap<>();

    static {
        extensionToLanguageMap.put("java", "Java");
        extensionToLanguageMap.put("py", "Python");
        extensionToLanguageMap.put("js", "JavaScript");
        extensionToLanguageMap.put("jsx", "JavaScript");
        extensionToLanguageMap.put("ts", "TypeScript");
        extensionToLanguageMap.put("tsx", "TypeScript");
        extensionToLanguageMap.put("cpp", "C++");
        extensionToLanguageMap.put("c++", "C++");
        extensionToLanguageMap.put("c", "C");
        extensionToLanguageMap.put("cs", "C#");
        extensionToLanguageMap.put("rb", "Ruby");
        extensionToLanguageMap.put("go", "Go");
        extensionToLanguageMap.put("php", "PHP");
        extensionToLanguageMap.put("html", "HTML");
        extensionToLanguageMap.put("htm", "HTML");
        extensionToLanguageMap.put("css", "CSS");
        extensionToLanguageMap.put("scss", "CSS");
        extensionToLanguageMap.put("less", "CSS");
        extensionToLanguageMap.put("swift", "Swift");
        extensionToLanguageMap.put("kt", "Kotlin");
        extensionToLanguageMap.put("kts", "Kotlin");
        extensionToLanguageMap.put("sql", "SQL");
        extensionToLanguageMap.put("xml", "XML");
        extensionToLanguageMap.put("yml", "YAML");
        extensionToLanguageMap.put("yaml", "YAML");
        extensionToLanguageMap.put("json", "JSON");
        extensionToLanguageMap.put("md", "Markdown");
        extensionToLanguageMap.put("sh", "Shell");
        extensionToLanguageMap.put("bat", "Batch");
        extensionToLanguageMap.put("pl", "Perl");
        extensionToLanguageMap.put("pm", "Perl");
        extensionToLanguageMap.put("r", "R");
        extensionToLanguageMap.put("m", "MATLAB");
        extensionToLanguageMap.put("mat", "MATLAB");
        extensionToLanguageMap.put("jl", "Julia");
        extensionToLanguageMap.put("rs", "Rust");
        extensionToLanguageMap.put("dart", "Dart");
        extensionToLanguageMap.put("scala", "Scala");
        extensionToLanguageMap.put("lua", "Lua");
        extensionToLanguageMap.put("groovy", "Groovy");
        extensionToLanguageMap.put("ini", "INI");
        extensionToLanguageMap.put("cfg", "Config");
        extensionToLanguageMap.put("toml", "TOML");
        extensionToLanguageMap.put("dockerfile", "Dockerfile");
        extensionToLanguageMap.put("makefile", "Makefile");
        extensionToLanguageMap.put("gradle", "Gradle");
    }

    public static String detectLanguageByExtension(File file) {
        String checkExt = file.getExtension();
        String extension = checkExt == null ? null : checkExt.toLowerCase();
        return extensionToLanguageMap.getOrDefault(extension, "Unknown");
    }

    public static String detectLanguageByExtension(String extension) {
        return extensionToLanguageMap.getOrDefault(extension, "Unknown");
    }

}