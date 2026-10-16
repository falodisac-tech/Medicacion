package com.futsch1.medtimer.feature.ui.medicine

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.futsch1.medtimer.feature.ui.R
import com.futsch1.medtimer.feature.ui.databinding.FragmentEditMedicineBinding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class EditMedicineFragment : Fragment() {

    private var _binding: FragmentEditMedicineBinding? = null
    private val binding get() = _binding!!

    private var selectedPrimaryColor: Int = Color.parseColor("#E53935")
    private var selectedSecondaryColor: Int = Color.parseColor("#FFFFFF")
    private var selectedShape: String = "capsule"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInstructionsDropdown()
        setupShapeToggleGroup()
        setupColorPickers()
    }

    private fun setupInstructionsDropdown() {
        val instructions = arrayOf(
            "En ayunas",
            "Con el desayuno",
            "Con el almuerzo",
            "Después de comer",
            "Antes de dormir",
            "Sin restricción"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, instructions)
        binding.actvInstruction.setAdapter(adapter)
    }

    private fun setupShapeToggleGroup() {
        binding.toggleShapeGroup.check(R.id.btnShapeCapsule)

        binding.toggleShapeGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnShapeCapsule -> {
                        selectedShape = "capsule"
                        binding.selectSecondaryColor.visibility = View.VISIBLE
                    }
                    R.id.btnShapeRound -> {
                        selectedShape = "round"
                        binding.selectSecondaryColor.visibility = View.GONE
                    }
                    R.id.btnShapeSyrup -> {
                        selectedShape = "syrup"
                        binding.selectSecondaryColor.visibility = View.GONE
                    }
                    R.id.btnShapeInjection -> {
                        selectedShape = "injection"
                        binding.selectSecondaryColor.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupColorPickers() {
        binding.selectColor.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setTitle("Seleccionar color principal")
                .setPositiveButton("Aceptar", ColorEnvelopeListener { envelope, _ ->
                    selectedPrimaryColor = envelope.color
                    binding.selectColor.setBackgroundColor(envelope.color)
                })
                .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                .attachAlphaSlideBar(false)
                .show()
        }

        binding.selectSecondaryColor.setOnClickListener {
            ColorPickerDialog.Builder(requireContext())
                .setTitle("Seleccionar color secundario (mitad cápsula)")
                .setPositiveButton("Aceptar", ColorEnvelopeListener { envelope, _ ->
                    selectedSecondaryColor = envelope.color
                    binding.selectSecondaryColor.setBackgroundColor(envelope.color)
                })
                .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                .attachAlphaSlideBar(false)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
