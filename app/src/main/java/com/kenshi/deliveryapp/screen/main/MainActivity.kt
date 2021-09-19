package com.kenshi.deliveryapp.screen.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kenshi.deliveryapp.R
import com.kenshi.deliveryapp.databinding.ActivityMainBinding
import com.kenshi.deliveryapp.screen.home.HomeFragment
import com.kenshi.deliveryapp.screen.my.MyFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() = with(binding){
        bottomNav.setOnNavigationItemSelectedListener(this@MainActivity)
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //프래그먼트 교체
        return when(item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
                true
            }

            R.id.menu_my -> {
                showFragment(MyFragment.newInstance(), MyFragment.TAG)
                true
            }
            else -> false
        }
    }

    //없으면 새로 생성해서 넣어주고 기존에 존재하면 fragmentContainer 에서 꺼내줌
    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        //supportFragmentsManager 에서 fragment 를 꺼내서 각각 hide 시켜줌(가려줌)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }

        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }
}