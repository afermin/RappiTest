package com.example.rappitest

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val navController = findNavController(R.id.container)

        // get fragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container)!!

        // setup custom navigator
        val navigator = CustomNavigator(this, navHostFragment.childFragmentManager, R.id.container)
        navController.navigatorProvider.addNavigator( navigator)

        // set navigation graph
        navController.setGraph(R.navigation.main)

        NavigationUI.setupWithNavController(navigation, findNavController(R.id.container))
        /*val navController = findNavController(R.id.container) as CustomNavigator
        navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.tvShowSearchFragment ->
                    navController.navigate(MovieFra, NavOptions.Builder().setClearTask(true).build())
                    navController.navigate(MainDirections.showTvShowSearch(), NavOptions.Builder().setClearTask(true).build())
                R.id.movieSearchFragment ->
                    navController.navigate(MainDirections.showMovieSearch(), NavOptions.Builder().setClearTask(true).build())
            }

            return@setOnNavigationItemSelectedListener true

        }*/
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

}

