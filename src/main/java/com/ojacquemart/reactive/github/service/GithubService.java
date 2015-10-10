package com.ojacquemart.reactive.github.service;

import com.ojacquemart.reactive.github.domain.GithubUser;
import com.ojacquemart.reactive.github.domain.RawUser;
import com.ojacquemart.reactive.github.domain.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.Arrays;

@Slf4j
@Service
public class GithubService {

    private RestClient restClient = new RestClient();

    public GithubUser getUser(String login) {
        Observable<RawUser> getRawUser = getRawUserObservable(login);
        Observable<RawUser[]> getFollowers = getFollowersObservable(login);
        Observable<Repository[]> getRepositories = getReposObservable(login);

        Observable<GithubUser> fullUser = Observable.zip(Arrays.asList(getRawUser, getFollowers, getRepositories), objects -> {
            RawUser rawUser = (RawUser) objects[0];
            RawUser[] followers = (RawUser[]) objects[1];
            Repository[] repositories = (Repository[]) objects[2];

            return new GithubUser(rawUser, Arrays.asList(followers), Arrays.asList(repositories));
        });

        return fullUser.toBlocking().first();
    }

    private Observable<RawUser> getRawUserObservable(String login) {
        return Observable.create((Subscriber<? super RawUser> s) -> s.onNext(restClient.getUser(login)))
                .onErrorReturn(throwable -> {
                    log.error("Failed to retrieve user {}", login, throwable);

                    return new RawUser("???", null, null);
                })
                .subscribeOn(Schedulers.computation());
    }

    private Observable<RawUser[]> getFollowersObservable(String login) {
        return Observable.create((Subscriber<? super RawUser[]> s) -> s.onNext(restClient.getFollowers(login)))
                .onErrorReturn(throwable -> {
                    log.error("Failed to retrieve {} followers", login, throwable);

                    return new RawUser[]{};
                })
                .subscribeOn(Schedulers.computation());
    }

    private Observable<Repository[]> getReposObservable(String login) {
        return Observable.create((Subscriber<? super Repository[]> s) -> s.onNext(restClient.getRepositories(login)))
                .onErrorReturn(throwable -> {
                    log.error("Failed to retrieve {} repos", login, throwable);

                    return new Repository[]{};
                })
                .subscribeOn(Schedulers.computation());
    }

}
