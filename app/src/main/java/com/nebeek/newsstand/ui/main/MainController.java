package com.nebeek.newsstand.ui.main;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.support.RouterPagerAdapter;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.controller.base.BaseBackStackController;
import com.nebeek.newsstand.controller.base.BaseController;
import com.nebeek.newsstand.controller.base.HeaderController;
import com.nebeek.newsstand.data.DataRepository;
import com.nebeek.newsstand.data.local.PreferenceManager;
import com.nebeek.newsstand.ui.library.LibraryController;

import butterknife.BindView;

public class MainController extends HeaderController implements MainContract.View {

    private String[] pageTitles = {"Read Later", "Explore", "Library", "For You"};

    private int NUMBER_OF_TABS = 4;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

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
                controllers[0] = new LibraryController();
                break;

            case 1:
                controllers[1] = new LibraryController();
                break;

            case 2:
                controllers[2] = new LibraryController();
                break;

            default:
                controllers[3] = new LibraryController();
                break;
        }

        controllers[position].setTargetController(MainController.this);
        return controllers[position];
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        super.onViewBound(view);

        headerBackButton.setVisibility(View.INVISIBLE);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(NUMBER_OF_TABS - 1);
        viewPager.setOffscreenPageLimit(NUMBER_OF_TABS - 1);

        setActive(true);
        presenter = new MainPresenter(PreferenceManager.getInstance(getActivity()), DataRepository.getInstance(), this);
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
}
