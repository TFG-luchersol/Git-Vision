package org.springframework.samples.gitvision.file.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * <p>The class <strong>TreeFiles</strong> is used for structuring a set of folders.
 * This structure does not allow the existence of child nodes
 * of the same parent that have the same name.</p>
 *
 * <p>A <strong>TreeNode</strong> is the instance where the name of
 * the node and information about the child nodes is stored.</p>
 */
public class TreeFiles {

    public static class TreeNode {

        private static final String NAME_ROOT = "/";

        private String name;

        private List<TreeNode> children;

        @JsonProperty
        private Map<String, Object> information;

        public TreeNode(String name) {
            this.name = name;
            this.children = new ArrayList<>();
            this.information = new HashMap<>();
        }

        public static TreeNode root(){
            return new TreeNode(NAME_ROOT);
        }

        public String getName() {
            return name;
        }

        public String getExtension(){
            return isLeaf() ? new File(this.name).getExtension() : null;
        }

        public List<TreeNode> getChildren() {
            return children;
        }

        public <T> T getInformation(String typeInformation, Class<T> clazz){
            if (clazz == null)
                throw new IllegalArgumentException("Class parameter cannot be null");

            Object value = information.get(typeInformation);

            if (!clazz.isInstance(value))
                throw new ClassCastException("Cannot cast " + value.getClass().getName() + " to " + clazz.getName());

            return clazz.cast(value);
        }

        public void put(String key, Object value) {
            information.put(key, value);
        }

        public void addChild(TreeNode child) {
            children.add(child);
        }

        public TreeNode getChildByName(String name) {
            return children.stream().filter(child -> child.name.equals(name)).findFirst().orElse(null);
        }

        public boolean hasSingleChild() {
            return children.size() == 1;
        }

        public boolean isLeaf(){
            return children.size() == 0;
        }

        @JsonIgnore
        public TreeNode getSingleChild() {
            return hasSingleChild() ? children.get(0) : null;
        }
    }

    public static TreeNode buildTree(List<String> paths) {
        TreeNode root = TreeNode.root();

        for (String path : paths) {
            String[] parts = path.split("/");
            TreeNode current = root;

            for (String part: parts) {
                TreeNode child = current.getChildByName(part);
                if(child == null){
                    child = new TreeNode(part);
                    current.addChild(child);
                }
                current = child;
            }
        }
        return root;
    }

    public static TreeNode buildTreeWithCollapse(List<String> paths) {
        TreeNode root = buildTree(paths);
        collapseSingleChildNodes(root);
        return root;
    }

    public static TreeNode buildTreeFromFiles(List<File> files){
        List<String> paths = files.stream().map(File::getPath).toList();
        return buildTree(paths);
    }

    public static TreeNode buildTreeFromFilesWithCollapse(List<File> files){
        List<String> paths = files.stream().map(File::getPath).toList();
        return buildTreeWithCollapse(paths);
    }

    private static void collapseSingleChildNodes(TreeNode node) {
        for (TreeNode child : new ArrayList<>(node.getChildren())) {
            collapseSingleChildNodes(child);

            if (child.hasSingleChild()) {
                TreeNode grandChild = child.getSingleChild();
                String newKey = child.getName() + "/" + grandChild.getName();
                child.name = newKey;
                child.children = grandChild.children;
            }
        }
    }

    public static void printTree(TreeNode node, String indent) {
        System.out.println(indent + node.getName());
        node.getChildren().forEach(child -> printTree(child, indent + " "));
    }

}
