package by.itransition.webconstructor.dto;

import by.itransition.webconstructor.domain.Site;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SiteDto {

    private String name;

    private String description;

    private String logo;

    private final String[] allMenu = {"vertical", "horizontal"};

    private List<String> menus = new ArrayList<>(0);

    private final String[] allThemes = {"fair", "dark"};

    private String theme = "dark";

    public SiteDto(Site site) {
        this.name = site.getName();
        this.description = site.getDescription();
        this.logo = site.getLogo();
        if (this.logo == null || this.logo.length() == 0) {
            this.logo = "rwkhctdn9wyli2cvwfxn"; //FIXME loh
        }
    }

}
