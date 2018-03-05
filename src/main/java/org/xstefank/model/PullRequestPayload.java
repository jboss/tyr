package org.xstefank.model;

import org.eclipse.egit.github.core.Label;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Team;
import org.eclipse.egit.github.core.User;

import java.util.List;

public class PullRequestPayload {

    String action;

    int number;

    PullRequest pull_request;

    List<User> requested_reviewers;

    List<Team> requested_teams;

    List<Label> labels;


}
