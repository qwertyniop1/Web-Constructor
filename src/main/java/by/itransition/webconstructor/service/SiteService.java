package by.itransition.webconstructor.service;

import by.itransition.webconstructor.domain.Site;

import java.util.List;

public interface SiteService {

    List<Site> getSites(String user);

}
