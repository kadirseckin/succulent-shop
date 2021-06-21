package school.cactus.succulentshop.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentSignupBinding
import school.cactus.succulentshop.infra.BaseFragment

class SignupFragment : BaseFragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override val viewModel: SignupViewModel by viewModels {
        SignupViewModelFactory(
            store = JwtStore(requireContext()),
            repository = SignupRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.sign_up)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}