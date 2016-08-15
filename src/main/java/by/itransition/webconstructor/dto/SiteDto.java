package by.itransition.webconstructor.dto;

import by.itransition.webconstructor.domain.MenuOrientation;
import by.itransition.webconstructor.domain.Site;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SiteDto {

    private static final String VERTICAL = "vertical";
    private static final String HORIZONTAL = "horizontal";

    @NotNull
    @Size(min = 5, max = 30)
    private String name;

    @NotNull
    @Size(min = 30, max = 255)
    private String description;

    private String logo;

    private List<String> tags = new ArrayList<>(0);

    private final String[] allMenu = {VERTICAL, HORIZONTAL};

    private List<String> menus = new ArrayList<>(0);

    private final String[] allThemes = {"fair", "dark"};

    private String theme = "dark";

    public SiteDto(Site site) {
        this.name = site.getName();
        this.description = site.getDescription();
        this.logo = site.getLogo();
        if (site.getMenuOrientation() == MenuOrientation.BOTH) {
            this.menus.add(VERTICAL);
            this.menus.add(HORIZONTAL);
        }
        if (site.getMenuOrientation() == MenuOrientation.VERTICAL) {
            this.menus.add(VERTICAL);
        }
        if (site.getMenuOrientation() == MenuOrientation.HORIZONTAL) {
            this.menus.add(HORIZONTAL);
        }
    }

}
