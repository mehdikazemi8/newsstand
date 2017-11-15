package com.nebeek.newsstand.ui.main;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.ui.explore.ExploreController;
import com.nebeek.newsstand.ui.subscribes.SubscribesController;
import com.nebeek.newsstand.ui.topic.TopicController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainController extends BaseController implements MainContract.View {

    private String[] pageTitles = {"Read Later", "Explore", "Library", "For You"};

    private int NUMBER_OF_TABS = 4;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.search_box)
    FloatingSearchView searchBox;

    public static MainController newInstance() {
        return new MainController();
    }

    private MainContract.Presenter presenter;
    private final RouterPagerAdapter pagerAdapter;
    private BaseBackStackController[] controllers = new BaseBackStackController[NUMBER_OF_TABS];

    public MainController() {
        pagerAdapter = new RouterPagerAdapter(this) {
            @Override
            public void configureRouter(@NonNull Router router, int position) {
                if (!router.hasRootController()) {
                    router.setRoot(RouterTransaction.with(getPage(position)));
                }
            }

            @Override
            public int getCount() {
                return NUMBER_OF_TABS;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return pageTitles[position];
            }
        };
        pagerAdapter.setMaxPagesToStateSave(NUMBER_OF_TABS);
    }

    private BaseController getPage(int position) {
        switch (position) {
            case 0:
                controllers[0] = new ExploreController();
                break;

            case 1:
                controllers[1] = new ExploreController();
                break;

            case 2:
                controllers[2] = new ExploreController();
                break;

            default:
                controllers[3] = new SubscribesController();
                break;
        }

        controllers[position].setTargetController(MainController.this);
        return controllers[position];
    }


    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        PreferenceManager.getInstance(getActivity()).getFcmID();

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(NUMBER_OF_TABS - 1);
        viewPager.setOffscreenPageLimit(NUMBER_OF_TABS - 1);

        searchBox.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                presenter.onSuggestionClicked(searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });

        searchBox.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                presenter.getTopics(newQuery);
            }
        });

        searchBox.setOnMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                Log.d("TAG", "abcd onMenuOpened");
            }

            @Override
            public void onMenuClosed() {
                Log.d("TAG", "abcd onMenuClosed");
            }
        });

        setActive(true);
        presenter = new MainPresenter(PreferenceManager.getInstance(getActivity()), DataRepository.getInstance(), this);
        searchView.setOnQueryTextListener(presenter);
        presenter.start();
    }

    @Override
    protected View inflateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_main, container, false);
    }

    @Override
    public boolean handleBack() {
        if (controllers[viewPager.getCurrentItem()].canHandleBackStack()) {
            return true;
        } else {
            if (viewPager.getCurrentItem() != NUMBER_OF_TABS - 1) {
                viewPager.setCurrentItem(NUMBER_OF_TABS - 1);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    protected void onDestroyView(View view) {
        if (!getActivity().isChangingConfigurations()) {
            viewPager.setAdapter(null);
        }
        tabLayout.setupWithViewPager(null);
        super.onDestroyView(view);
    }

    @Override
    public void showSearchUI(Topic topic) {
        getRouter().pushController(
                RouterTransaction.with(TopicController.newInstance(topic))
                        .pushChangeHandler(new FadeChangeHandler())
                        .popChangeHandler(new FadeChangeHandler())
        );
    }

    public static class TopicSuggestion implements SearchSuggestion {

        private String topicName;
        private boolean isHistory = false;

        public TopicSuggestion(String suggestion) {
            this.topicName = suggestion.toLowerCase();
        }

        public TopicSuggestion(Parcel source) {
            this.topicName = source.readString();
            this.isHistory = source.readInt() != 0;
        }

        public void setIsHistory(boolean isHistory) {
            this.isHistory = isHistory;
        }

        public boolean getIsHistory() {
            return this.isHistory;
        }

        @Override
        public String getBody() {
            return topicName;
        }

        public static final Creator<TopicSuggestion> CREATOR = new Creator<TopicSuggestion>() {
            @Override
            public TopicSuggestion createFromParcel(Parcel in) {
                return new TopicSuggestion(in);
            }

            @Override
            public TopicSuggestion[] newArray(int size) {
                return new TopicSuggestion[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(topicName);
            dest.writeInt(isHistory ? 1 : 0);
        }
    }

    @Override
    public void showSuggestions(List<String> suggestions) {
        List<TopicSuggestion> newList = new ArrayList<>();
        for (String suggestion : suggestions) {
            newList.add(new TopicSuggestion(suggestion));
        }

        searchBox.swapSuggestions(newList);
    }
}
