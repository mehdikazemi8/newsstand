package com.nebeek.newsstand.ui.main;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.nebeek.newsstand.ui.bookmark.BookmarkController;
import com.nebeek.newsstand.ui.explore.ExploreController;
import com.nebeek.newsstand.ui.search.SearchController;
import com.nebeek.newsstand.ui.subscribes.SubscribesController;

import butterknife.BindView;
import butterknife.OnClick;

public class MainController extends BaseController implements MainContract.View {

//    private String[] pageTitles = {"پروفایل", "آرشیو", "آخرین اخبار", "منتخب شما"};
//    private String[] pageIcons = {"Q", "H", "S", "R"};

    private String[] pageTitles = {"آرشیو", "منتخب شما", "آخرین اخبار"};
    private String[] pageIcons = {"H", "R", "S"};

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
                controllers[1] = new SubscribesController();
                break;

            default:
                controllers[2] = new ExploreController();
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


}
