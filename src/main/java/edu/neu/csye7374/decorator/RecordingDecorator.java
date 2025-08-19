package edu.neu.csye7374.decorator;

import edu.neu.csye7374.event.EventAPI;

public class RecordingDecorator extends EventDecorator {
    private final boolean availableAfterEvent;
    private final String recordingLink; // optional

    public RecordingDecorator(EventAPI event, boolean availableAfterEvent, String recordingLink) {
        super(event);
        this.availableAfterEvent = availableAfterEvent;
        this.recordingLink = recordingLink;
    }

    @Override
    public String getDescription() {
        String base = super.getDescription();
        String linkPart = (recordingLink != null && !recordingLink.isEmpty())
                ? " (" + recordingLink + ")"
                : "";
        return base + System.lineSeparator()
                + "Recording: " + (availableAfterEvent ? "Available" : "Not available") + linkPart;
    }
}
