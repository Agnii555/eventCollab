package edu.neu.csye7374.decorator;

import edu.neu.csye7374.event.EventAPI;

public class OnlineStreamingDecorator extends EventDecorator {
    private final String platform;
    private final String streamUrl; // optional

    public OnlineStreamingDecorator(EventAPI event, String platform, String streamUrl) {
        super(event);
        this.platform = platform;
        this.streamUrl = streamUrl;
    }

    @Override
    public String getDescription() {
        String base = super.getDescription();
        String urlPart = (streamUrl != null && !streamUrl.isEmpty()) ? " (" + streamUrl + ")" : "";
        return base + System.lineSeparator() + "Streaming: " + platform + urlPart;
    }
}
