package com.shoppi.kotlin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.shoppi.kotlin.R
import com.shoppi.kotlin.common.KEY_PRODUCT_ID
import com.shoppi.kotlin.databinding.FragmentHomeBinding
import com.shoppi.kotlin.ui.categorydetail.CategoryPromotionAdapter
import com.shoppi.kotlin.ui.categorydetail.CategorySectionTitleAdapter
import com.shoppi.kotlin.ui.common.EventObserver
import com.shoppi.kotlin.ui.common.ProductClickListener
import com.shoppi.kotlin.ui.common.ViewModelFactory

class HomeFragment : Fragment(), ProductClickListener {

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory(
            requireContext()
        )
    }
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*이 작업은 필수*/
        binding.lifecycleOwner = viewLifecycleOwner

        setToolbar()
        setTopBanners()
        setNavigation()
        setListAdapter()
    }

    override fun onProductClick(productId: String) {
        findNavController().navigate(
            R.id.action_home_to_product_detail, bundleOf(
                KEY_PRODUCT_ID to "desk-1"
            )
        )
    }

    private fun setNavigation() {
        viewModel.openProductDetailEvent.observe(viewLifecycleOwner, EventObserver { productId ->
            findNavController().navigate(
                R.id.action_home_to_product_detail, bundleOf(
                    KEY_PRODUCT_ID to productId
                )
            )
        })
    }


    private fun setToolbar() {
        viewModel.title.observe(
            viewLifecycleOwner
        ) { title ->
            binding.title = title
        }
    }

    private fun setTopBanners() {
        with(binding.vpHomeBanner) {
            adapter = HomeBannerAdapter(viewModel).apply {
                viewModel.topBanners.observe(viewLifecycleOwner) { banners ->
                    submitList(banners)
                }
            }

            val pageWidth = resources.getDimension(R.dimen.viewpager_item_width)
            val pageMargin = resources.getDimension(R.dimen.viewpager_item_margin)

            val screenWidth = resources.displayMetrics.widthPixels
            val offset = screenWidth - pageWidth - pageMargin

            offscreenPageLimit = 3
            setPageTransformer { page, position ->
                page.translationX = position * -offset
            }

            TabLayoutMediator(
                binding.vpHomeBannerIndicator, this
            ) { tab, position -> tab.text = "" }.attach()
        }
    }

    private fun setListAdapter() {
        val titleAdapter = CategorySectionTitleAdapter()
        val promotionAdapter = CategoryPromotionAdapter(this)
        binding.rvHomeRecommendations.adapter = ConcatAdapter(
            titleAdapter, promotionAdapter
        )
        viewModel.promotions.observe(viewLifecycleOwner) { promotions ->
            titleAdapter.submitList(listOf(promotions.title))
            promotionAdapter.submitList(promotions.items)
            promotionAdapter.setOnItemClickListener(object : CategoryPromotionAdapter.OnItemClickListener {
                override fun onItemClick(v: View, productId: String) {
                }
            })
        }
    }

}
