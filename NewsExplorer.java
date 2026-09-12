import java.util.*;
import java.util.concurrent.*;

// ============================================================
// NEWS EXPLORER
// Data Structures and Algorithms - 3
// ============================================================

public class NewsExplorer {

    // ========================================================
    // ARTICLE CLASS
    // ========================================================

    static class Article {
        int id;
        String title;
        String category;
        String content;
        int readingTime;
        int relevance;
        int views;

        Article(int id, String title, String category,
                String content, int readingTime,
                int relevance, int views) {

            this.id = id;
            this.title = title;
            this.category = category;
            this.content = content;
            this.readingTime = readingTime;
            this.relevance = relevance;
            this.views = views;
        }

        @Override
        public String toString() {
            return "\n[" + id + "] " + title +
                    "\nCategory: " + category +
                    "\nReading Time: " + readingTime + " min" +
                    "\nRelevance: " + relevance +
                    "\nViews: " + views +
                    "\n" + content;
        }
    }

    // ========================================================
    // SAMPLE NEWS DATABASE
    // ========================================================

    static ArrayList<Article> articles = new ArrayList<>();

    static void loadArticles() {

        articles.add(new Article(
                1,
                "India launches new artificial intelligence mission",
                "Technology",
                "India announces a national artificial intelligence mission "
                        + "to promote research, innovation and digital technology.",
                5, 9, 9500
        ));

        articles.add(new Article(
                2,
                "Hyderabad technology companies expand AI research",
                "Technology",
                "Technology companies in Hyderabad are increasing investments "
                        + "in artificial intelligence and machine learning research.",
                4, 8, 7200
        ));

        articles.add(new Article(
                3,
                "India wins international cricket championship",
                "Sports",
                "The Indian cricket team wins an important international "
                        + "championship after an exciting final match.",
                3, 10, 15000
        ));

        articles.add(new Article(
                4,
                "New education policy introduces digital learning",
                "Education",
                "Universities introduce digital learning platforms and "
                        + "technology based education methods.",
                6, 7, 5400
        ));

        articles.add(new Article(
                5,
                "Indian scientists develop renewable energy technology",
                "Science",
                "Scientists develop a new renewable energy technology "
                        + "that can improve clean electricity generation.",
                7, 9, 8100
        ));

        articles.add(new Article(
                6,
                "Artificial intelligence transforms Indian healthcare",
                "Technology",
                "Artificial intelligence is being used to improve "
                        + "healthcare diagnosis and medical research.",
                5, 9, 8900
        ));

        articles.add(new Article(
                7,
                "Hyderabad hosts major technology innovation event",
                "Technology",
                "Hyderabad hosts a technology event featuring startups, "
                        + "artificial intelligence and digital innovation.",
                4, 8, 6500
        ));

        articles.add(new Article(
                8,
                "India announces major space research program",
                "Science",
                "India announces a new space research program focused "
                        + "on advanced scientific exploration.",
                6, 8, 7300
        ));

        articles.add(new Article(
                9,
                "Indian universities expand computer science programs",
                "Education",
                "Universities increase computer science courses and "
                        + "advanced technology education opportunities.",
                5, 7, 4700
        ));

        articles.add(new Article(
                10,
                "Cricket team prepares for upcoming world tournament",
                "Sports",
                "The Indian cricket team begins preparation for an upcoming "
                        + "international world tournament.",
                3, 8, 11000
        ));
    }

    // ========================================================
    // 1. KMP STRING MATCHING
    // ========================================================

    static int[] buildLPS(String pattern) {

        int[] lps = new int[pattern.length()];

        int length = 0;
        int i = 1;

        while (i < pattern.length()) {

            if (pattern.charAt(i) == pattern.charAt(length)) {

                length++;
                lps[i] = length;
                i++;

            } else {

                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }

        return lps;
    }

    static boolean KMP(String text, String pattern) {

        if (pattern.isEmpty())
            return true;

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        int[] lps = buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == pattern.length())
                    return true;

            } else {

                if (j != 0)
                    j = lps[j - 1];
                else
                    i++;
            }
        }

        return false;
    }

    // ========================================================
    // 2. Z ALGORITHM
    // ========================================================

    static int[] calculateZ(String s) {

        int n = s.length();

        int[] z = new int[n];

        int left = 0;
        int right = 0;

        for (int i = 1; i < n; i++) {

            if (i <= right)
                z[i] = Math.min(right - i + 1, z[i - left]);

            while (i + z[i] < n &&
                    s.charAt(z[i]) == s.charAt(i + z[i])) {

                z[i]++;
            }

            if (i + z[i] - 1 > right) {

                left = i;
                right = i + z[i] - 1;
            }
        }

        return z;
    }

    static boolean ZSearch(String text, String pattern) {

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        String combined = pattern + "$" + text;

        int[] z = calculateZ(combined);

        for (int value : z) {

            if (value == pattern.length())
                return true;
        }

        return false;
    }

    // ========================================================
    // 3. RABIN-KARP
    // ========================================================

    static boolean RabinKarp(String text, String pattern) {

        text = text.toLowerCase();
        pattern = pattern.toLowerCase();

        if (pattern.length() > text.length())
            return false;

        int prime = 101;
        int base = 256;

        int patternHash = 0;
        int textHash = 0;

        int h = 1;

        for (int i = 0; i < pattern.length() - 1; i++)
            h = (h * base) % prime;

        for (int i = 0; i < pattern.length(); i++) {

            patternHash =
                    (base * patternHash + pattern.charAt(i)) % prime;

            textHash =
                    (base * textHash + text.charAt(i)) % prime;
        }

        for (int i = 0;
             i <= text.length() - pattern.length();
             i++) {

            if (patternHash == textHash) {

                boolean match = true;

                for (int j = 0; j < pattern.length(); j++) {

                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }

                if (match)
                    return true;
            }

            if (i < text.length() - pattern.length()) {

                textHash =
                        (base *
                                (textHash -
                                        text.charAt(i) * h)
                                +
                                text.charAt(i + pattern.length()))
                                % prime;

                if (textHash < 0)
                    textHash += prime;
            }
        }

        return false;
    }

    // ========================================================
    // 4. EDIT DISTANCE
    // ========================================================

    static int editDistance(String a, String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int m = a.length();
        int n = b.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++)
            dp[i][0] = i;

        for (int j = 0; j <= n; j++)
            dp[0][j] = j;

        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                if (a.charAt(i - 1) ==
                        b.charAt(j - 1)) {

                    dp[i][j] = dp[i - 1][j - 1];

                } else {

                    dp[i][j] =
                            1 + Math.min(
                                    dp[i - 1][j - 1],
                                    Math.min(
                                            dp[i - 1][j],
                                            dp[i][j - 1]
                                    )
                            );
                }
            }
        }

        return dp[m][n];
    }

    // ========================================================
    // 5. SEARCH USING KMP
    // ========================================================

    static void searchKMP(String query) {

        System.out.println("\n===== KMP SEARCH =====");

        boolean found = false;

        for (Article article : articles) {

            String text =
                    article.title + " " + article.content;

            if (KMP(text, query)) {

                System.out.println(article);
                found = true;
            }
        }

        if (!found)
            System.out.println("No article found.");
    }

    // ========================================================
    // 6. SEARCH USING Z ALGORITHM
    // ========================================================

    static void searchZ(String query) {

        System.out.println("\n===== Z ALGORITHM SEARCH =====");

        boolean found = false;

        for (Article article : articles) {

            String text =
                    article.title + " " + article.content;

            if (ZSearch(text, query)) {

                System.out.println(article);
                found = true;
            }
        }

        if (!found)
            System.out.println("No article found.");
    }

    // ========================================================
    // 7. SEARCH USING RABIN-KARP
    // ========================================================

    static void searchRabinKarp(String query) {

        System.out.println("\n===== RABIN-KARP SEARCH =====");

        boolean found = false;

        for (Article article : articles) {

            String text =
                    article.title + " " + article.content;

            if (RabinKarp(text, query)) {

                System.out.println(article);
                found = true;
            }
        }

        if (!found)
            System.out.println("No article found.");
    }

    // ========================================================
    // 8. FUZZY SEARCH USING EDIT DISTANCE
    // ========================================================

    static void fuzzySearch(String query) {

        System.out.println("\n===== FUZZY SEARCH =====");

        for (Article article : articles) {

            int distance =
                    editDistance(
                            query.toLowerCase(),
                            article.title.toLowerCase()
                    );

            if (distance <= 8) {

                System.out.println(
                        article.id +
                        ". " +
                        article.title +
                        " | Edit Distance = " +
                        distance
                );
            }
        }
    }

    // ========================================================
    // 9. SUFFIX ARRAY
    // ========================================================

    static class Suffix implements Comparable<Suffix> {

        int index;
        String suffix;

        Suffix(int index, String suffix) {
            this.index = index;
            this.suffix = suffix;
        }

        @Override
        public int compareTo(Suffix other) {
            return suffix.compareTo(other.suffix);
        }
    }

    static int[] buildSuffixArray(String text) {

        int n = text.length();

        Suffix[] suffixes = new Suffix[n];

        for (int i = 0; i < n; i++) {

            suffixes[i] =
                    new Suffix(i, text.substring(i));
        }

        Arrays.sort(suffixes);

        int[] suffixArray = new int[n];

        for (int i = 0; i < n; i++)
            suffixArray[i] = suffixes[i].index;

        return suffixArray;
    }

    // ========================================================
    // 10. LCP ARRAY
    // ========================================================

    static int[] buildLCP(String text, int[] suffixArray) {

        int n = text.length();

        int[] rank = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++)
            rank[suffixArray[i]] = i;

        int k = 0;

        for (int i = 0; i < n; i++) {

            if (rank[i] == n - 1) {

                k = 0;
                continue;
            }

            int j = suffixArray[rank[i] + 1];

            while (i + k < n &&
                    j + k < n &&
                    text.charAt(i + k) ==
                            text.charAt(j + k)) {

                k++;
            }

            lcp[rank[i]] = k;

            if (k > 0)
                k--;
        }

        return lcp;
    }

    static void suffixArrayDemo(String text) {

        System.out.println("\n===== SUFFIX ARRAY + LCP =====");

        int[] suffixArray =
                buildSuffixArray(text);

        int[] lcp =
                buildLCP(text, suffixArray);

        for (int i = 0; i < suffixArray.length; i++) {

            System.out.println(
                    "SA[" + i + "] = " +
                            suffixArray[i] +
                            " | LCP = " +
                            lcp[i] +
                            " | " +
                            text.substring(
                                    suffixArray[i]
                            )
            );
        }
    }

    // ========================================================
    // 11. BITMASK DP
    // PERSONALIZED NEWS SELECTION
    // ========================================================

    static void personalizedSelection(int budget) {

        System.out.println(
                "\n===== BITMASK DP PERSONALIZED NEWS ====="
        );

        int n = Math.min(articles.size(), 10);

        int totalStates = 1 << n;

        int bestMask = 0;
        int bestScore = 0;

        for (int mask = 0;
             mask < totalStates;
             mask++) {

            int time = 0;
            int score = 0;

            for (int i = 0; i < n; i++) {

                if ((mask & (1 << i)) != 0) {

                    time += articles.get(i).readingTime;

                    score += articles.get(i).relevance;
                }
            }

            if (time <= budget &&
                    score > bestScore) {

                bestScore = score;
                bestMask = mask;
            }
        }

        System.out.println(
                "Reading time budget: " +
                        budget + " minutes"
        );

        System.out.println(
                "Maximum relevance score: " +
                        bestScore
        );

        System.out.println("Selected articles:");

        for (int i = 0; i < n; i++) {

            if ((bestMask & (1 << i)) != 0) {

                System.out.println(
                        "- " +
                                articles.get(i).title
                );
            }
        }
    }

    // ========================================================
    // 12. TREE DP
    // TOPIC CATEGORY RELEVANCE
    // ========================================================

    static class TopicNode {

        String name;
        int relevance;
        ArrayList<TopicNode> children =
                new ArrayList<>();

        TopicNode(String name, int relevance) {

            this.name = name;
            this.relevance = relevance;
        }
    }

    static int topicTreeDP(TopicNode node) {

        int total = node.relevance;

        for (TopicNode child : node.children)
            total += topicTreeDP(child);

        return total;
    }

    static void treeDPDemo() {

        System.out.println(
                "\n===== TREE DP TOPIC RELEVANCE ====="
        );

        TopicNode technology =
                new TopicNode("Technology", 10);

        TopicNode ai =
                new TopicNode("Artificial Intelligence", 8);

        TopicNode programming =
                new TopicNode("Programming", 6);

        TopicNode cybersecurity =
                new TopicNode("Cyber Security", 7);

        technology.children.add(ai);
        technology.children.add(programming);
        technology.children.add(cybersecurity);

        System.out.println(
                "Total Technology relevance = " +
                        topicTreeDP(technology)
        );
    }

    // ========================================================
    // 13. DINIC'S MAX FLOW
    // ========================================================

    static class Edge {

        int to;
        int capacity;
        int reverse;

        Edge(int to, int capacity, int reverse) {

            this.to = to;
            this.capacity = capacity;
            this.reverse = reverse;
        }
    }

    static class Dinic {

        int n;
        ArrayList<Edge>[] graph;
        int[] level;
        int[] ptr;

        Dinic(int n) {

            this.n = n;

            graph =
                    new ArrayList[n];

            for (int i = 0; i < n; i++)
                graph[i] = new ArrayList<>();

            level = new int[n];
            ptr = new int[n];
        }

        void addEdge(int from,
                     int to,
                     int capacity) {

            Edge forward =
                    new Edge(
                            to,
                            capacity,
                            graph[to].size()
                    );

            Edge backward =
                    new Edge(
                            from,
                            0,
                            graph[from].size()
                    );

            graph[from].add(forward);
            graph[to].add(backward);
        }

        boolean bfs(int source, int sink) {

            Arrays.fill(level, -1);

            Queue<Integer> queue =
                    new LinkedList<>();

            queue.add(source);

            level[source] = 0;

            while (!queue.isEmpty()) {

                int v = queue.poll();

                for (Edge edge : graph[v]) {

                    if (edge.capacity > 0 &&
                            level[edge.to] == -1) {

                        level[edge.to] =
                                level[v] + 1;

                        queue.add(edge.to);
                    }
                }
            }

            return level[sink] != -1;
        }

        int dfs(int v,
                int sink,
                int pushed) {

            if (pushed == 0)
                return 0;

            if (v == sink)
                return pushed;

            while (ptr[v] <
                    graph[v].size()) {

                Edge edge =
                        graph[v].get(ptr[v]);

                if (level[edge.to] ==
                        level[v] + 1 &&
                        edge.capacity > 0) {

                    int flow =
                            dfs(
                                    edge.to,
                                    sink,
                                    Math.min(
                                            pushed,
                                            edge.capacity
                                    )
                            );

                    if (flow > 0) {

                        edge.capacity -= flow;

                        graph[edge.to]
                                .get(edge.reverse)
                                .capacity += flow;

                        return flow;
                    }
                }

                ptr[v]++;
            }

            return 0;
        }

        int maxFlow(int source, int sink) {

            int flow = 0;

            while (bfs(source, sink)) {

                Arrays.fill(ptr, 0);

                int pushed;

                while ((pushed =
                        dfs(
                                source,
                                sink,
                                Integer.MAX_VALUE
                        )) > 0) {

                    flow += pushed;
                }
            }

            return flow;
        }
    }

    static void networkFlowDemo() {

        System.out.println(
                "\n===== NETWORK FLOW ====="
        );

        /*
             Source
            /      \
        Crawler1 Crawler2
         /   \     /   \
       News1 News2 News3
            \      /
              Sink
        */

        Dinic dinic =
                new Dinic(7);

        int source = 0;
        int crawler1 = 1;
        int crawler2 = 2;
        int news1 = 3;
        int news2 = 4;
        int news3 = 5;
        int sink = 6;

        dinic.addEdge(
                source,
                crawler1,
                2
        );

        dinic.addEdge(
                source,
                crawler2,
                2
        );

        dinic.addEdge(
                crawler1,
                news1,
                1
        );

        dinic.addEdge(
                crawler1,
                news2,
                1
        );

        dinic.addEdge(
                crawler2,
                news2,
                1
        );

        dinic.addEdge(
                crawler2,
                news3,
                1
        );

        dinic.addEdge(
                news1,
                sink,
                1
        );

        dinic.addEdge(
                news2,
                sink,
                1
        );

        dinic.addEdge(
                news3,
                sink,
                1
        );

        int maxFlow =
                dinic.maxFlow(
                        source,
                        sink
                );

        System.out.println(
                "Maximum resource allocation = " +
                        maxFlow
        );
    }

    // ========================================================
    // 14. BIPARTITE MATCHING
    // ALERT TO USER MATCHING
    // ========================================================

    static boolean bpm(
            int user,
            boolean[][] graph,
            boolean[] visited,
            int[] match) {

        for (int article = 0;
             article < graph[user].length;
             article++) {

            if (graph[user][article] &&
                    !visited[article]) {

                visited[article] = true;

                if (match[article] == -1 ||
                        bpm(
                                match[article],
                                graph,
                                visited,
                                match
                        )) {

                    match[article] = user;

                    return true;
                }
            }
        }

        return false;
    }

    static void bipartiteMatchingDemo() {

        System.out.println(
                "\n===== BIPARTITE MATCHING ====="
        );

        /*
          Users -> Breaking News Alerts

          User 1 -> AI, Cricket
          User 2 -> Cricket, Education
          User 3 -> AI, Science
        */

        boolean[][] graph = {

                {true, true, false, false},

                {false, true, true, false},

                {true, false, false, true}
        };

        int users = graph.length;
        int[] match =
                new int[4];

        Arrays.fill(match, -1);

        int result = 0;

        for (int user = 0;
             user < users;
             user++) {

            boolean[] visited =
                    new boolean[4];

            if (bpm(
                    user,
                    graph,
                    visited,
                    match
            )) {

                result++;
            }
        }

        System.out.println(
                "Maximum alert-user matches = " +
                        result
        );
    }

    // ========================================================
    // 15. SHINGLING
    // ========================================================

    static Set<String> shingles(
            String text,
            int k) {

        text = text.toLowerCase();

        Set<String> set =
                new HashSet<>();

        String[] words =
                text.split("\\s+");

        for (int i = 0;
             i <= words.length - k;
             i++) {

            StringBuilder shingle =
                    new StringBuilder();

            for (int j = 0; j < k; j++) {

                if (j > 0)
                    shingle.append(" ");

                shingle.append(words[i + j]);
            }

            set.add(shingle.toString());
        }

        return set;
    }

    // ========================================================
    // 16. MINHASH
    // ========================================================

    static int minHash(
            Set<String> shingles,
            int seed) {

        int minimum =
                Integer.MAX_VALUE;

        for (String shingle : shingles) {

            int hash =
                    Objects.hash(
                            shingle,
                            seed
                    );

            minimum =
                    Math.min(
                            minimum,
                            hash
                    );
        }

        return minimum;
    }

    static int[] minHashSignature(
            String text) {

        Set<String> set =
                shingles(text, 3);

        int[] signature =
                new int[20];

        for (int i = 0;
             i < signature.length;
             i++) {

            signature[i] =
                    minHash(set, i + 1);
        }

        return signature;
    }

    static double minHashSimilarity(
            int[] a,
            int[] b) {

        int same = 0;

        for (int i = 0; i < a.length; i++) {

            if (a[i] == b[i])
                same++;
        }

        return (double) same / a.length;
    }

    // ========================================================
    // 17. LSH
    // ========================================================

    static String lshBucket(int[] signature) {

        StringBuilder key =
                new StringBuilder();

        for (int i = 0;
             i < 4 && i < signature.length;
             i++) {

            key.append(signature[i] % 10)
                    .append("-");
        }

        return key.toString();
    }

    static void minHashLshDemo() {

        System.out.println(
                "\n===== MINHASH + LSH ====="
        );

        String a =
                "India develops artificial intelligence technology "
                        + "for digital innovation";

        String b =
                "India develops new artificial intelligence technology "
                        + "for digital innovation";

        int[] sigA =
                minHashSignature(a);

        int[] sigB =
                minHashSignature(b);

        double similarity =
                minHashSimilarity(
                        sigA,
                        sigB
                );

        System.out.println(
                "Estimated similarity = " +
                        String.format(
                                "%.2f",
                                similarity
                        )
        );

        System.out.println(
                "LSH bucket A = " +
                        lshBucket(sigA)
        );

        System.out.println(
                "LSH bucket B = " +
                        lshBucket(sigB)
        );
    }

    // ========================================================
    // 18. APPROXIMATION ALGORITHM
    // GREEDY SET COVER
    // ========================================================

    static void approximationDemo() {

        System.out.println(
                "\n===== APPROXIMATION ALGORITHM ====="
        );

        Map<String, Set<String>> sources =
                new LinkedHashMap<>();

        sources.put(
                "Source A",
                new HashSet<>(
                        Arrays.asList(
                                "AI",
                                "Sports",
                                "Science"
                        )
                )
        );

        sources.put(
                "Source B",
                new HashSet<>(
                        Arrays.asList(
                                "AI",
                                "Education"
                        )
                )
        );

sources.put(
        "Source C",
        new HashSet<>(
                Arrays.asList(
                        "Sports",
                        "Education",
                        "Science"
                )
        )
);

        Set<String> universe =
                new HashSet<>(
                        Arrays.asList(
                                "AI",
                                "Sports",
                                "Science",
                                "Education"
                        )
                );

        Set<String> covered =
                new HashSet<>();

        ArrayList<String> selected =
                new ArrayList<>();

        while (!covered.equals(universe)) {

            String bestSource = null;

            int bestGain = 0;

            for (String source :
                    sources.keySet()) {

                Set<String> topics =
                        sources.get(source);

                int gain = 0;

                for (String topic :
                        topics) {

                    if (!covered.contains(topic))
                        gain++;
                }

                if (gain > bestGain) {

                    bestGain = gain;
                    bestSource = source;
                }
            }

            if (bestSource == null)
                break;

            selected.add(bestSource);

            covered.addAll(
                    sources.get(bestSource)
            );

            sources.remove(bestSource);
        }

        System.out.println(
                "Selected sources: " +
                        selected
        );

        System.out.println(
                "Topics covered: " +
                        covered
        );
    }

    // ========================================================
    // 19. PARALLEL PREFIX SUM
    // ========================================================

    static int[] parallelPrefixSum(
            int[] array) {

        int[] result =
                Arrays.copyOf(
                        array,
                        array.length
                );

        int processors =
                Math.min(
                        4,
                        array.length
                );

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        processors
                );

        try {

            Future<?>[] futures =
                    new Future<?>[processors];

            int chunk =
                    (array.length +
                            processors - 1)
                            / processors;

            /*
             * First compute local prefix sums.
             */

            for (int p = 0;
                 p < processors;
                 p++) {

                final int start =
                        p * chunk;

                final int end =
                        Math.min(
                                array.length,
                                start + chunk
                        );

                final int processor = p;

                futures[p] =
                        executor.submit(() -> {

                            for (int i = start + 1;
                                 i < end;
                                 i++) {

                                result[i] +=
                                        result[i - 1];
                            }
                        });
            }

            for (Future<?> future :
                    futures) {

                try {
                    future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /*
             * Sequential adjustment between chunks.
             */

            int offset = 0;

            for (int p = 0;
                 p < processors;
                 p++) {

                int start =
                        p * chunk;

                int end =
                        Math.min(
                                array.length,
                                start + chunk
                        );

                if (start >= end)
                    continue;

                int localTotal =
                        result[end - 1];

                for (int i = start;
                     i < end;
                     i++) {

                    result[i] += offset;
                }

                offset += localTotal;
            }

        } finally {

            executor.shutdown();
        }

        return result;
    }

    // ========================================================
    // 20. PARALLEL REDUCE
    // ========================================================

    static long parallelReduce(
            int[] array) {

        int processors =
                Math.min(
                        4,
                        array.length
                );

        ExecutorService executor =
                Executors.newFixedThreadPool(
                        processors
                );

        ArrayList<Future<Long>> futures =
                new ArrayList<>();

        int chunk =
                (array.length +
                        processors - 1)
                        / processors;

        for (int p = 0;
             p < processors;
             p++) {

            final int start =
                    p * chunk;

            final int end =
                    Math.min(
                            array.length,
                            start + chunk
                    );

            futures.add(
                    executor.submit(() -> {

                        long sum = 0;

                        for (int i = start;
                             i < end;
                             i++) {

                            sum += array[i];
                        }

                        return sum;
                    })
            );
        }

        long total = 0;

        try {

            for (Future<Long> future :
                    futures) {

                total += future.get();
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            executor.shutdown();
        }

        return total;
    }

    // ========================================================
    // 21. TRENDING ANALYSIS
    // ========================================================

    static void trendingAnalysis() {

        System.out.println(
                "\n===== PARALLEL TRENDING ANALYSIS ====="
        );

        int[] views =
                new int[articles.size()];

        for (int i = 0;
             i < articles.size();
             i++) {

            views[i] =
                    articles.get(i).views;
        }

        int[] prefix =
                parallelPrefixSum(views);

        long total =
                parallelReduce(views);

        System.out.println(
                "Total views = " +
                        total
        );

        System.out.println(
                "Prefix Sum:"
        );

        System.out.println(
                Arrays.toString(prefix)
        );

        System.out.println(
                "\nTrending Articles:"
        );

        ArrayList<Article> sorted =
                new ArrayList<>(
                        articles
                );

        sorted.sort(
                (a, b) ->
                        Integer.compare(
                                b.views,
                                a.views
                        )
        );

        for (int i = 0;
             i < Math.min(5, sorted.size());
             i++) {

            Article article =
                    sorted.get(i);

            System.out.println(
                    (i + 1) +
                            ". " +
                            article.title +
                            " | Views = " +
                            article.views
            );
        }
    }

    // ========================================================
    // 22. DISPLAY ALL ARTICLES
    // ========================================================

    static void displayArticles() {

        System.out.println(
                "\n========== ALL NEWS =========="
        );

        for (Article article :
                articles) {

            System.out.println(
                    article.id +
                            ". " +
                            article.title +
                            " [" +
                            article.category +
                            "]"
            );
        }
    }

    // ========================================================
    // 23. MAIN MENU
    // ========================================================

    public static void main(String[] args) {

        loadArticles();

        Scanner scanner =
                new Scanner(System.in);

        while (true) {

            System.out.println(
                    "\n\n======================================"
            );

            System.out.println(
                    "          NEWS EXPLORER"
            );

            System.out.println(
                    "      DATA STRUCTURES & ALGORITHMS"
            );

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "1. Display All News"
            );

            System.out.println(
                    "2. Search using KMP"
            );

            System.out.println(
                    "3. Search using Z-Algorithm"
            );

            System.out.println(
                    "4. Search using Rabin-Karp"
            );

            System.out.println(
                    "5. Fuzzy Search - Edit Distance"
            );

            System.out.println(
                    "6. Suffix Array + LCP Demo"
            );

            System.out.println(
                    "7. Personalized News - Bitmask DP"
            );

            System.out.println(
                    "8. Topic Relevance - Tree DP"
            );

            System.out.println(
                    "9. Network Flow - Dinic"
            );

            System.out.println(
                    "10. Bipartite Matching"
            );

            System.out.println(
                    "11. MinHash + LSH"
            );

            System.out.println(
                    "12. Approximation Algorithm"
            );

            System.out.println(
                    "13. Parallel Prefix Sum + Reduce"
            );

            System.out.println(
                    "14. Run Complete Demonstration"
            );

            System.out.println(
                    "0. Exit"
            );

            System.out.print(
                    "\nEnter your choice: "
            );

            int choice;

            try {

                choice =
                        scanner.nextInt();

            } catch (Exception e) {

                scanner.nextLine();

                System.out.println(
                        "Please enter a number."
                );

                continue;
            }

            scanner.nextLine();

            switch (choice) {

                case 1:

                    displayArticles();

                    break;

                case 2:

                    System.out.print(
                            "Enter keyword: "
                    );

                    searchKMP(
                            scanner.nextLine()
                    );

                    break;

                case 3:

                    System.out.print(
                            "Enter keyword: "
                    );

                    searchZ(
                            scanner.nextLine()
                    );

                    break;

                case 4:

                    System.out.print(
                            "Enter keyword: "
                    );

                    searchRabinKarp(
                            scanner.nextLine()
                    );

                    break;

                case 5:

                    System.out.print(
                            "Enter search text: "
                    );

                    fuzzySearch(
                            scanner.nextLine()
                    );

                    break;

                case 6:

                    suffixArrayDemo(
                            "artificial intelligence"
                    );

                    break;

                case 7:

                    System.out.print(
                            "Enter reading time budget: "
                    );

                    int budget =
                            scanner.nextInt();

                    personalizedSelection(
                            budget
                    );

                    break;

                case 8:

                    treeDPDemo();

                    break;

                case 9:

                    networkFlowDemo();

                    break;

                case 10:

                    bipartiteMatchingDemo();

                    break;

                case 11:

                    minHashLshDemo();

                    break;

                case 12:

                    approximationDemo();

                    break;

                case 13:

                    trendingAnalysis();

                    break;

                case 14:

                    runCompleteDemo();

                    break;

                case 0:

                    System.out.println(
                            "\nThank you for using News Explorer!"
                    );

                    scanner.close();

                    return;

                default:

                    System.out.println(
                            "Invalid choice."
                    );
            }
        }
    }

    // ========================================================
    // COMPLETE DEMO
    // ========================================================

    static void runCompleteDemo() {

        System.out.println(
                "\n\n========================================"
        );

        System.out.println(
                "     NEWS EXPLORER COMPLETE DEMO"
        );

        System.out.println(
                "========================================"
        );

        searchKMP("artificial intelligence");

        searchZ("technology");

        searchRabinKarp("India");

        fuzzySearch("artifical intelligence");

        suffixArrayDemo(
                "banana"
        );

        personalizedSelection(15);

        treeDPDemo();

        networkFlowDemo();

        bipartiteMatchingDemo();

        minHashLshDemo();

        approximationDemo();

        trendingAnalysis();

        System.out.println(
                "\n========================================"
        );

        System.out.println(
                "       DEMONSTRATION COMPLETED"
        );

        System.out.println(
                "========================================"
        );
    }
}