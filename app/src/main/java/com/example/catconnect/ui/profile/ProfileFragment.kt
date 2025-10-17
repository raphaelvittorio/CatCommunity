package com.example.catconnect.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.catconnect.MainActivity
import com.example.catconnect.R
import com.example.catconnect.data.session.SessionManager
import com.example.catconnect.databinding.FragmentProfileBinding
import com.example.catconnect.ui.feed.PostAdapter
import com.google.android.material.snackbar.Snackbar

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val vm: ProfileViewModel by viewModels()
    private lateinit var adapter: PostAdapter
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbarAndDrawer()

        // header user
        binding.imgAvatar.load(vm.user.photoUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        binding.tvName.text = vm.user.name
        binding.tvBio.text = vm.user.bio

        // list post milik user
        adapter = PostAdapter(
            onClick = { post ->
                val b = Bundle().apply { putString("postId", post.id) }
                findNavController().navigate(R.id.postDetailFragment, b)
            },
            onLike = { post ->
                vm.likePost(post)
                Snackbar.make(requireView(), "Liked", Snackbar.LENGTH_LONG)
                    .setAction("Undo") { vm.unlikePost(post) }
                    .show()
            },
            onComment = { _ ->
                Snackbar.make(requireView(), "Comment is not available from this screen", Snackbar.LENGTH_SHORT).show()
            },
            onShare = { _ ->
                Snackbar.make(requireView(), "Share is not available from this screen", Snackbar.LENGTH_SHORT).show()
            },
            onSave = { _ ->
                Snackbar.make(requireView(), "Save is not available from this screen", Snackbar.LENGTH_SHORT).show()
            }
        )
        binding.rvMyPosts.adapter = adapter
        binding.rvMyPosts.layoutManager = LinearLayoutManager(requireContext())

        vm.myPosts.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        // Swipe-to-delete
        val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.bindingAdapterPosition
                if (pos == RecyclerView.NO_POSITION) return
                val item = adapter.currentList[pos]
                vm.deletePost(item)
                Snackbar.make(requireView(), "Deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") { vm.addPost() }
                    .show()
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(binding.rvMyPosts)
    }

    private fun setupToolbarAndDrawer() {
        val navController = findNavController()
        // Konfigurasi AppBar dengan drawer layout, ini akan menampilkan ikon hamburger
        appBarConfiguration = AppBarConfiguration(setOf(R.id.profileFragment), binding.profileDrawerLayout)

        // Hubungkan toolbar milik fragment ini dengan NavController
        binding.profileToolbar.setupWithNavController(navController, appBarConfiguration)

        binding.profileNavView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.action_logout) {
                SessionManager(requireContext()).logout()
                // Untuk mencegah navigasi ke login jika sudah di sana atau sedang dalam proses
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
        // Sembunyikan AppBar utama milik MainActivity saat halaman ini muncul
        (activity as? MainActivity)?.showAppBar(false)
    }

    override fun onPause() {
        super.onPause()
        // Tampilkan kembali AppBar utama saat halaman ini ditinggalkan
        (activity as? MainActivity)?.showAppBar(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
