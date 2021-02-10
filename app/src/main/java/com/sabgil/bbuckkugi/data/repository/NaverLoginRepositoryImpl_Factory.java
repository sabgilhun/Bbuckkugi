// Generated by Dagger (https://dagger.dev).
package com.sabgil.bbuckkugi.data.repository;

import android.app.Activity;
import android.content.Context;
import com.sabgil.bbuckkugi.data.api.NaverLoginApi;
import dagger.internal.Factory;
import javax.inject.Provider;

@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class NaverLoginRepositoryImpl_Factory implements Factory<NaverLoginRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<Activity> activityProvider;

  private final Provider<NaverLoginApi> naverLoginApiProvider;

  public NaverLoginRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<Activity> activityProvider, Provider<NaverLoginApi> naverLoginApiProvider) {
    this.contextProvider = contextProvider;
    this.activityProvider = activityProvider;
    this.naverLoginApiProvider = naverLoginApiProvider;
  }

  @Override
  public NaverLoginRepositoryImpl get() {
    return newInstance(contextProvider.get(), activityProvider.get(), naverLoginApiProvider.get());
  }

  public static NaverLoginRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<Activity> activityProvider, Provider<NaverLoginApi> naverLoginApiProvider) {
    return new NaverLoginRepositoryImpl_Factory(contextProvider, activityProvider, naverLoginApiProvider);
  }

  public static NaverLoginRepositoryImpl newInstance(Context context, Activity activity,
      NaverLoginApi naverLoginApi) {
    return new NaverLoginRepositoryImpl(context, activity, naverLoginApi);
  }
}
