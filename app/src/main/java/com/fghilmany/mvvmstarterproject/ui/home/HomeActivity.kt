package com.fghilmany.mvvmstarterproject.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.fghilmany.mvvmstarterproject.R
import com.fghilmany.mvvmstarterproject.core.data.Resource
import com.fghilmany.mvvmstarterproject.core.utils.PreferenceProvider
import com.fghilmany.mvvmstarterproject.databinding.ActivityHomeBinding
import com.fghilmany.mvvmstarterproject.ui.add.AddStoryActivity
import com.fghilmany.mvvmstarterproject.ui.login.LoginActivity
import com.fghilmany.mvvmstarterproject.ui.paging.LoadingStateAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: HomeViewModel by viewModel()

    private val adapter = HomeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        initUI()

    }

    private fun getData() {
        binding.rvStory.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter =
                this@HomeActivity.adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter{
                        this@HomeActivity.adapter.retry()
                    }
                )
        }
        viewModel.getStories().observe(this) {
            adapter.submitData(lifecycle, it)
        }

    }

    private fun initUI() {
        with(binding) {
            tvName.text = PreferenceProvider(this@HomeActivity).getName()
            ibLogout.setOnClickListener {
                Intent(this@HomeActivity, LoginActivity::class.java)
                    .apply {
                        startActivity(this)
                        finishAffinity()
                        PreferenceProvider(this@HomeActivity).clearPreference()
                    }
            }

            fabAddNewStory.setOnClickListener {
                Intent(this@HomeActivity, AddStoryActivity::class.java)
                    .apply {
                        startActivity(this)
                    }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.not_getting_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}