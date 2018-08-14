package br.com.leandro.retrofitpokemonapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import br.com.leandro.retrofitpokemonapi.PokemonApi.PokemonApiService;
import br.com.leandro.retrofitpokemonapi.models.Pokemon;
import br.com.leandro.retrofitpokemonapi.models.PokemonResposta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "POKEMON API ON";

    private Retrofit retrofit;

    private RecyclerView recyclerView;

    private ListaPokemonAdapter listaPokemonAdapter;

    private int offset;

    private boolean prontoParaCarregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listaPokemonAdapter = new ListaPokemonAdapter(this);
        recyclerView.setAdapter(listaPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (prontoParaCarregar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, " Llegamos al final.");

                            prontoParaCarregar = false;
                            offset += 20;
                            obterDados(offset);
                        }
                    }
                }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        prontoParaCarregar = true;
        offset = 0;
        obterDados(offset);


    }

    private void obterDados(int offset){

        PokemonApiService service = retrofit.create(PokemonApiService.class);
        Call <PokemonResposta> pokemonRespostaCall = service.obterListaPokemon(100,offset);

        pokemonRespostaCall.enqueue(new Callback<PokemonResposta>() {
            @Override
            public void onResponse(Call<PokemonResposta> call, Response<PokemonResposta> response) {

                if (response.isSuccessful()){

                    PokemonResposta pokemonResposta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonResposta.getResults();

                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);

                    // for(int i=0; i < listaPokemon.size(); i++){
                    //     Pokemon p = listaPokemon.get(i);
                    //     Log.i(TAG,"Pokemon :" + p.getName());
                    // }

                } else {
                    Log.e(TAG, "onResponse"+ response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<PokemonResposta> call, Throwable t) {

                prontoParaCarregar = true;
                Log.e(TAG, " onFailure: " + t.getMessage());

            }
        });


    }
}
