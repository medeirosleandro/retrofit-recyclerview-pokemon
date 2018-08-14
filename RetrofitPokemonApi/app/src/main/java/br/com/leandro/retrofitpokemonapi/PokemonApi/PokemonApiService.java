package br.com.leandro.retrofitpokemonapi.PokemonApi;

import br.com.leandro.retrofitpokemonapi.models.PokemonResposta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokemonApiService {


    @GET ("pokemon")

    Call<PokemonResposta>obterListaPokemon(@Query("limit") int limit, @Query("offset") int offset);
}
