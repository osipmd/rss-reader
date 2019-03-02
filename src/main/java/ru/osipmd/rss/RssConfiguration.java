package ru.osipmd.rss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.osipmd.rss.parameters.RssParameter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RssConfiguration {

    private String url;
    private String outputFileName;
    private Long pollPeriod;
    private Long itemsAmount;
    private Boolean turnOn;
    private Set<RssParameter> parameters;
    private Date lastPollTime;
}
