package nikalas.nunev.ejlistacompra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

import nikalas.nunev.ejlistacompra.adapters.ProductsAdapter;
import nikalas.nunev.ejlistacompra.databinding.ActivityMainBinding;
import nikalas.nunev.ejlistacompra.modelos.Product;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Product> productList;

    private ProductsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        productList = new ArrayList<>();

        adapter = new ProductsAdapter(productList,R.layout.product_view_model,MainActivity.this);
        layoutManager = new GridLayoutManager(this,2);

        binding.contentMain.container.setAdapter(adapter);
        binding.contentMain.container.setLayoutManager(layoutManager);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProduct().show();
            }
        });
    }

    private AlertDialog createProduct(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Create Product");
        builder.setCancelable(false);

        View productViewModel = LayoutInflater.from(this).inflate(R.layout.product_view_model, null);
        EditText txtName = productViewModel.findViewById(R.id.txtNameProductViewModel);
        EditText txtQuantity = productViewModel.findViewById(R.id.txtQuantityProductViewModel);
        EditText txtPrice = productViewModel.findViewById(R.id.txtPriceProductViewModel);
        TextView lbTotal = productViewModel.findViewById(R.id.lbTotalProductViewModel);

        builder.setView(productViewModel);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int quantity = Integer.parseInt(txtPrice.getText().toString());
                    float price = Float.parseFloat(txtPrice.getText().toString());
                    float total = quantity * price;

                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                    lbTotal.setText(numberFormat.format(total));
                }catch (Exception e){

                }
            }
        };
        txtQuantity.addTextChangedListener(textWatcher);
        txtPrice.addTextChangedListener(textWatcher);

        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (txtName.getText().toString().isEmpty() ||
                txtQuantity.getText().toString().isEmpty() ||
                txtPrice.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Missing Data", Toast.LENGTH_SHORT).show();
                }else {
                    Product product = new Product(
                            txtName.getText().toString(),
                            Integer.parseInt(txtQuantity.getText().toString()),
                            Float.parseFloat(txtPrice.getText().toString())
                    );

                    productList.add(0,product);
                    adapter.notifyItemInserted(0);
                    Toast.makeText(MainActivity.this, product.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }
}