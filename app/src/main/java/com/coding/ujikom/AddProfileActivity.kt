package com.coding.ujikom

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.coding.ujikom.databinding.ActivityAddProfileBinding
import java.util.Calendar

class AddProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProfileBinding

    private var UriImage: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                UriImage = result.data!!.data
                binding.ivProfile.setImageURI(UriImage)
            } else {
                Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnUpload.setOnClickListener {
            val intentGalery = Intent()
            intentGalery.action = Intent.ACTION_GET_CONTENT
            intentGalery.type = "image/*"
            pickImageLauncher.launch(intentGalery)
        }
        binding.edtDate.setOnClickListener {
            showDatePicker()
        }

        binding.btnAdd.setOnClickListener {
            addDataProfile(binding)
        }


    }

    private fun addDataProfile(binding: ActivityAddProfileBinding) {
        val name = binding.edtNama.text.toString()
        val nim = binding.edtNim.text.toString()
        val date = binding.edtDate.text.toString()
        val place = binding.edtPlace.text.toString()
        val selectedGenderId = binding.radioGroup.checkedRadioButtonId

        if (name.isEmpty()) {
            binding.edtNama.error = "Nama harus diisi"
            binding.edtNama.requestFocus()
            return
        }

        if (nim.isEmpty()) {
            binding.edtNim.error = "NIM harus diisi"
            binding.edtNim.requestFocus()
            return
        }

        if (date.isEmpty()) {
            binding.edtDate.error = "Tanggal lahir harus diisi"
            binding.edtDate.requestFocus()
            return
        }

        if (place.isEmpty()) {
            binding.edtPlace.error = "Tempat lahir harus diisi"
            binding.edtPlace.requestFocus()
            return
        }


        val gender = if (selectedGenderId == R.id.rb_male) {
            "Laki-laki"
        } else {
            "Perempuan"
        }
        if (gender.isEmpty()) {
            Toast.makeText(this, "Mohon pilih jenis kelamin", Toast.LENGTH_SHORT).show()
            return
        }
        val profile = Profile(name, nim, gender, date, place, UriImage.toString())
        val intent = Intent(this, EditProfileActivity::class.java).apply {
            putExtra("profile", profile)
        }
        startActivity(intent)
    }



    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%02d-%02d-%d", selectedDay, selectedMonth+1, selectedYear)
                binding.edtDate.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}