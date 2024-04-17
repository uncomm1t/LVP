class Dot implements Clerk {
    final String visLibOnlinePath = "https://unpkg.com/vis-network/standalone/umd/vis-network.min.js";
    final String visLibOfflinePath = "clerks/Dot/vis-network.min.js";
    final String dotLibPath = "clerks/Dot/dot.js";
    final String ID;
    LiveView view;
    int width, height;

    Dot(LiveView view, int width, int height) {
        this.view = view;
        this.width = width;
        this.height = height;

        Clerk.load(view, visLibOnlinePath, visLibOfflinePath);
        Clerk.load(view, dotLibPath);

        ID = Clerk.getHashID(this);

        Clerk.write(view, STR."""
            <div id="dotContainer\{ID}">
            </div>
                """);
        Clerk.script(view, STR."const dot\{ID} = new Dot(document.getElementById('dotContainer\{ID}'), \{this.width}, \{this.height});");
    }

    Dot(LiveView view) { this(view, 500, 500); }
    Dot(int width, int height) { this(Clerk.view(), width, height); }
    Dot() { this(Clerk.view());}

    Dot draw(String dotString) {
        String escaped = dotString.replaceAll("\\\"", "\\\\\"").replaceAll("\\n", "");
        Clerk.script(view, STR."dot\{ID}.draw(\"dinetwork{\{escaped}}\")");
        return this;
    }
}