// ChatState
//package cs149.chat;

import java.util.LinkedList;

public class ChatState {
    private static final int MAX_HISTORY = 32;
    private static final long TIMEOUT = 15000;

    private final String name;
    private final LinkedList<String> history = new LinkedList<String>();
    private long lastID = System.currentTimeMillis();

    public ChatState(final String name) {
        this.name = name;
        history.addLast("Hello " + name + "!");
    }

    public String getName() {
        return name;
    }

    public synchronized void addMessage(final String msg) {
        int currSize = history.size();
        if (currSize == MAX_HISTORY) {
          history.remove();
        }
        history.add(msg);
        (this.lastID)++;
        this.notifyAll();
    }

    public synchronized String recentMessages(long mostRecentSeenID) {
        if (lastID <= mostRecentSeenID) {
          try {
	    this.wait(TIMEOUT);
	  } catch (InterruptedException e) {
				
          }
        }

        String result = "";
        for (int i = history.size() - 1; i >= 0; i--) {
          long id = lastID - i;
          if (id > mostRecentSeenID) {
            int position = history.size() - i - 1;
            result += id + ": " + history.get(position) + "\n";
          }
        }

        return result;
    }
}
