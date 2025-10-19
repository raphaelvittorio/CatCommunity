package com.example.catconnect.ui.profile

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.catconnect.MainActivity
import com.example.catconnect.R
import com.example.catconnect.data.repo.FakeDb
import com.example.catconnect.data.session.SessionManager
import com.example.catconnect.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by viewModels()
    private lateinit var adapter: PostGridAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbarAndDrawer()

        // Set OnClickListener untuk tombol Edit Profile
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        // Setup RecyclerView with a 3-column grid
        adapter = PostGridAdapter { post ->
            val b = Bundle().apply { putString("postId", post.id) }
            findNavController().navigate(R.id.postDetailFragment, b)
        }

        binding.rvMyPosts.adapter = adapter
        binding.rvMyPosts.layoutManager = GridLayoutManager(requireContext(), 3)
        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.rvMyPosts.addItemDecoration(GridSpacingItemDecoration(3, spacing, true))


        vm.myPosts.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun loadUserData() {
        val currentUser = FakeDb.currentUser
        binding.imgAvatar.load(currentUser.photoUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
            error(R.drawable.ic_cat_face)
        }
        binding.tvName.text = currentUser.name
        binding.tvBio.text = currentUser.bio
    }

    private fun setupToolbarAndDrawer() {
        // Set ikon navigasi (hamburger) di toolbar agar terlihat
        binding.profileToolbar.setNavigationIcon(R.drawable.ic_menu)

        // Hubungkan ikon navigasi toolbar untuk membuka drawer secara manual
        binding.profileToolbar.setNavigationOnClickListener {
            binding.profileDrawerLayout.open()
        }

        // Atur listener untuk item di dalam navigation view (untuk logout)
        binding.profileNavView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.action_logout) {
                SessionManager(requireContext()).logout()
                if (findNavController().currentDestination?.id != R.id.loginFragment) {
                    findNavController().navigate(R.id.action_global_to_login)
                }
                binding.profileDrawerLayout.closeDrawers()
                true
            } else {
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Selalu muat data terbaru setiap kali fragment ditampilkan
        loadUserData()
        (activity as? MainActivity)?.showAppBar(false)
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.showAppBar(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}
