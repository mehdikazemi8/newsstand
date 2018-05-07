package com.nebeek.newsstand.ui.main;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.nebeek.newsstand.ui.bookmark.BookmarkController;
import com.nebeek.newsstand.ui.explore.ExploreController;
import com.nebeek.newsstand.ui.search.SearchController;
import com.nebeek.newsstand.ui.subscribes.SubscribesController;
import com.nebeek.newsstand.ui.topic.TopicController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//import com.lapism.searchview.Search;
//import com.lapism.searchview.widget.SearchAdapter;
//import com.lapism.searchview.widget.SearchItem;
//import com.lapism.searchview.widget.SearchView;

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
    @BindView(R.id.search_icon_container)
    ConstraintLayout searchIconContainer;

    @OnClick(R.id.search_icon_container)
    public void searchContainerOnClick() {
        getRouter().pushController(
                RouterTransaction.with(SearchController.newInstance())
                        .pushChangeHandler(new FadeChangeHandler())
                        .popChangeHandler(new FadeChangeHandler())
        );
    }

    public static MainController newInstance() {
        return new MainController();
    }

    //    private SearchAdapter searchAdapter;
//    private List<SearchItem> suggestionsList;
//    private SearchView mSearchView;
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

        setActive(true);
        presenter = new MainPresenter(PreferenceManager.getInstance(getActivity()), DataRepository.getInstance(), this);
        presenter.start();

//        setupSearchView(view);

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

    /*
    private void setupSearchView(View view) {
        mSearchView = view.findViewById(R.id.searchView); // from API 26
        if (mSearchView != null) {

            mSearchView.setVersionMargins(Search.VersionMargins.TOOLBAR);
            mSearchView.setHint(R.string.search_in_categories);
            mSearchView.setOnQueryTextListener(
                    new Search.OnQueryTextListener() {
                        @Override
                        public void onQueryTextChange(CharSequence newText) {
//                            if (newText.equals(lastQuery)) {
//                                return false;
//                            }
                            lastQuery = newText.toString();
                            presenter.getTopics(newText.toString());
                        }

                        @Override
                        public boolean onQueryTextSubmit(CharSequence query) {
                            // when search icon on keyboard is clicked
                            Log.d("TAG", "abcd query " + query);
                            return false;
                        }
                    }
            );

            suggestionsList = new ArrayList<>();
//            searchAdapter = new SearchAdapter(getActivity(), suggestionsList);
            searchAdapter = new SearchAdapter(getActivity());
            searchAdapter.setSuggestionsList(suggestionsList);
            searchAdapter.setOnSearchItemClickListener((position, title, subtitle) -> {
                Log.d("TAG", "onSearchItemClick " + title);
                Log.d("TAG", "onSearchItemClick " + searchAdapter.getItemCount());

                mSearchView.close();
                mSearchView.setQuery("", false);
                presenter.onSuggestionClicked(title.toString());
            });

            mSearchView.setAdapter(searchAdapter);

//            List<SearchFilter> filter = new ArrayList<>();
//            filter.add(new SearchFilter("Filter1", true));
//            filter.add(new SearchFilter("Filter2", true));
//            mSearchView.setFilters(filter);
//            use mSearchView.getFiltersStates() to consider filter when performing search
        }
    }
*/

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

        Log.d("TAG", "abcd--- " + suggestions.size());
        for (String str : suggestions) {
            Log.d("TAG", "abcd--- " + str);
        }

        /*
        suggestionsList.clear();

        for (String suggestion : suggestions) {
            SearchItem item = new SearchItem(getActivity());
//            item.setSubtitle(suggestion);
            item.setTitle(suggestion + lastQuery);
//            item.set
            suggestionsList.add(item);
        }

//        SearchHistoryTable searchHistoryTable = new SearchHistoryTable(getActivity());
//        searchHistoryTable.getAllItems();
//
        Log.d("TAG", "abcd sizeeeeee 111 " + suggestionsList.size());
        // todo use setData, check if it animates
        searchAdapter.setSuggestionsList(suggestionsList);
        Log.d("TAG", "abcd sizeeeeee 222 " + searchAdapter.getSuggestionsList().size());
        searchAdapter.notifyDataSetChanged();
        Log.d("TAG", "abcd sizeeeeee 333 " + searchAdapter.getSuggestionsList().size());
        */
    }
}
