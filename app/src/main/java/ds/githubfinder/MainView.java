package ds.githubfinder;

import java.util.List;

import ds.githubfinder.model.User;

interface MainView{
    public void showSearchResult(List<User> users);
}
