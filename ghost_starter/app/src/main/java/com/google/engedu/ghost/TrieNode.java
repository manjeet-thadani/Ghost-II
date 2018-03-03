package com.google.engedu.ghost;

import java.util.HashMap;

public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        HashMap<String, TrieNode> temp = children;

        for (int i = 0; i < s.length(); i++) {
            String key = String.valueOf(s.charAt(i));

            if (!temp.containsKey(key)) {
                temp.put(key, new TrieNode());
            }

            if (i == s.length() - 1)
                temp.get(key).isWord = true;

            temp = temp.get(key).children;
        }
    }

    /*
        temp = children
        for (i : s){
            if !temp.haschild(i)
                return false
            temp = temp.getchild(i).children
        }

        return temp.isword;

     */
    public boolean isWord(String s) {
        HashMap<String, TrieNode> temp = children;
        TrieNode node = null;

        String key = new String();
        for (int i = 0; i < s.length(); i++) {
            key = String.valueOf(s.charAt(i));

            if (!temp.containsKey(key))
                return false;
            node = temp.get(key);
            temp = temp.get(key).children;
        }

        return node.isWord;
    }

    public String getAnyWordStartingWith(String s) {
        HashMap<String, TrieNode> temp = children;
        TrieNode node = null;

        if (s == null || s.length() == 0) {
            return "apple";
        } else {
            for (int i = 0; i < s.length(); i++) {
                String key = String.valueOf(s.charAt(i));
                if (!temp.containsKey(key)) {
                    return null;
                } else {
                    node = temp.get(key);
                    temp = node.children;
                }
            }

            while (!node.isWord) {
                String key = temp.keySet().iterator().next();
                //for (String key : temp.keySet()) {
                node = temp.get(key);
                temp = node.children;

                s += key;
                //}
            }

            return s;
        }
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
