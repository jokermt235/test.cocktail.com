package org.mutalip.cocktail.model;
import org.mutalip.cocktail.instance.IConverterFactory;

import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

public class DrinkFactory extends Converter.Factory  implements IConverterFactory {
    @Override
    public Converter.Factory create() {
        return GsonConverterFactory.create();
    }
}
