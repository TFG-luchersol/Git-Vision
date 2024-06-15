package org.springframework.samples.gitVision.connection;

import org.kohsuke.github.GitHub;

public class Connection {
    
    private static GitHub gitHub;

    public static GitHub getGitHub(){
        return gitHub;
    }

    public static void connect(String login, String token){
        try {
            if(gitHub == null)
                gitHub = GitHub.connect(login, token);
        } catch (Exception e) {
            
        }
    }

    public static void disconnect(){
        gitHub = null;
    }



}
