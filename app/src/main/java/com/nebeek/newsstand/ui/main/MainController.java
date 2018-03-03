package com.nebeek.newsstand.ui.main;

import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchItem;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.ui.bookmark.BookmarkController;
import com.nebeek.newsstand.ui.explore.ExploreController;
import com.nebeek.newsstand.ui.subscribes.SubscribesController;
import com.nebeek.newsstand.ui.topic.TopicController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainController extends BaseController implements MainContract.View {

//    private String[] pageTitles = {"پروفایل", "آرشیو", "آخرین اخبار", "منتخب شما"};
//    private String[] pageIcons = {"Q", "H", "S", "R"};

    private String[] pageTitles = {"آرشیو", "آخرین اخبار", "منتخب شما"};
    private String[] pageIcons = {"H", "S", "R"};


    private int NUMBER_OF_TABS = 3;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.search_box)
    FloatingSearchView searchBox;

    public static MainController newInstance() {
        return new MainController();
    }

    private SearchAdapter searchAdapter;
    private List<SearchItem> suggestionsList;
    private com.lapism.searchview.SearchView mSearchView;
    private String lastQuery = null;

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
//            case 0:
//                controllers[0] = new TempController();
//                break;

            case 0:
                controllers[0] = BookmarkController.newInstance();
                break;

            case 1:
                controllers[1] = new ExploreController();
                break;

            default:
                controllers[2] = new SubscribesController();
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
//        searchView.setOnQueryTextListener(presenter);
        presenter.start();

        test(view);

        setUpTabLayout();
    }

    private void setUpTabLayout() {
        for (int tabId = 0; tabId < NUMBER_OF_TABS; tabId++) {
            View tabView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.template_tab, null
            );
            TextView tabText = tabView.findViewById(R.id.tab_text);
            TextView tabIcon = tabView.findViewById(R.id.tab_icon);
            tabText.setText(pageTitles[tabId]);
            tabIcon.setText(pageIcons[tabId]);
            tabLayout.getTabAt(tabId).setCustomView(tabView);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void test(View view) {
        mSearchView = view.findViewById(R.id.searchView); // from API 26
        if (mSearchView != null) {

            mSearchView.setVersionMargins(com.lapism.searchview.SearchView.VersionMargins.TOOLBAR_SMALL);
            mSearchView.setHint("جست و جو");
            mSearchView.setOnQueryTextListener(
                    new com.lapism.searchview.SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextChange(String newText) {

                            if (newText.equals(lastQuery)) {
                                return false;
                            }
                            lastQuery = newText;

                            Log.d("TAG", "abcd " + newText);
                            presenter.getTopics(newText);

                            //todo remove
//                            fakeAddSuggestions(newText);

                            return true;
                        }

                        @Override
                        public boolean onQueryTextSubmit(String query) {

                            // when search icon on keyboard is clicked
                            Log.d("TAG", "abcd query " + query);

                            return false;
                        }
                    }
            );

//            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
//                @Override
//                public boolean onOpen() {
//                    if (mFab != null) {
//                        mFab.hide();
//                    }
//                    return true;
//                }
//
//                @Override
//                public boolean onClose() {
//                    if (mFab != null && !mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//                        mFab.show();
//                    }
//                    return true;
//                }
//            });

//            mSearchView.setVoiceText("Set permission on Android 6.0+ !");
//            searchView.setOnVoiceIconClickListener(new SearchView.OnVoiceIconClickListener() {
//                @Override
//                public void onVoiceIconClick() {
//                     permission
//                }
//            });

            suggestionsList = new ArrayList<>();
//            suggestionsList.add(new SearchItem("search1"));
//            suggestionsList.add(new SearchItem("search2"));
//            suggestionsList.add(new SearchItem("search3"));

            searchAdapter = new SearchAdapter(getActivity(), suggestionsList);
            searchAdapter.setOnSearchItemClickListener((theView, position, text) -> {
                Log.d("TAG", "onSearchItemClick " + text);
                mSearchView.close(false);
                mSearchView.setQuery("", false);
                presenter.onSuggestionClicked(text);
            });

            mSearchView.setAdapter(searchAdapter);

//            suggestionsList.add(new SearchItem("tehran"));
//            suggestionsList.add(new SearchItem("tehban"));
//            suggestionsList.add(new SearchItem("tehraaaan"));
//            searchAdapter.notifyDataSetChanged();

//            List<SearchFilter> filter = new ArrayList<>();
//            filter.add(new SearchFilter("Filter1", true));
//            filter.add(new SearchFilter("Filter2", true));
//            mSearchView.setFilters(filter);
            //use mSearchView.getFiltersStates() to consider filter when performing search
        }
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

    private void fakeAddSuggestions(String newText) {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            items.add(newText + i);
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> showSuggestions(items), 200);
    }

    @Override
    public void showSuggestions(List<String> suggestions) {
//        List<TopicSuggestion> newList = new ArrayList<>();
//        for (String suggestion : suggestions) {
//            newList.add(new TopicSuggestion(suggestion));
//        }
//        searchBox.swapSuggestions(newList);

        Log.d("TAG", "abcd " + suggestions.size());
        for (String str : suggestions) {
            Log.d("TAG", "abcd " + str);
        }

        suggestionsList.clear();
        for (String suggestion : suggestions) {
            suggestionsList.add(new SearchItem(suggestion));
        }

        // todo use setData, check if it animates
//        searchAdapter.setData();
        searchAdapter.notifyDataSetChanged();
        mSearchView.setQuery(mSearchView.getQuery(), false);
//        mSearchView.showSuggestions();
//        searchAdapter.notifyItemRangeInserted(0, suggestions.size());
//        searchAdapter.setData(items);
    }
}
