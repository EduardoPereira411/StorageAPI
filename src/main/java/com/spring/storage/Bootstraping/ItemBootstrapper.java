package com.spring.storage.Bootstraping;

import com.spring.storage.CategoryManagement.model.Category;
import com.spring.storage.CategoryManagement.repositories.CategoryRepository;
import com.spring.storage.ItemManagement.model.Item;
import com.spring.storage.ItemManagement.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
public class ItemBootstrapper implements CommandLineRunner {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    private Category getOrCreateCategory(String name, String description) {
        return categoryRepository.findByName(name).orElseGet(() -> {
            Category newCategory = new Category(name, description);
            categoryRepository.save(newCategory);
            return newCategory;
        });
    }

    void createItem(String name, Category category) {
        if(itemRepository.findByName(name).isPresent()){

        }else{
            Item newItem = new Item(name, category);
            itemRepository.save(newItem);
        }
    }

    @Override
    public void run(final String... args) throws Exception {
        Category frutasCategory = getOrCreateCategory("Frutas", "Fontes de açúcar natural");
        Category cereaisCategory = getOrCreateCategory("Cereais", "Fontes de Fibra");
        Category carnesCategory = getOrCreateCategory("Carnes", "Fontes de Proteína");
        Category peixeCategory = getOrCreateCategory("Peixe", "Fontes de Proteína marítima");
        Category bebidasCategory = getOrCreateCategory("Bebidas", "Fontes de Hidratação");
        Category condimentosCategory = getOrCreateCategory("Condimentos", "Formas de Tempero");
        Category laticiniosCategory = getOrCreateCategory("Laticínios", "Fontes de Cálcio");
        Category legumesCategory = getOrCreateCategory("Legumes", "Items para complemento de uma refeição");
        Category petiscosCategory = getOrCreateCategory("Petiscos", "Gulosices para petiscar quando se tem fome");
        Category outrosCategory = getOrCreateCategory("Outros", "Items que não pertecem particularmente a nenhuma categoria");

        //FRUTAS
        createItem("Banana", frutasCategory);
        createItem("Maçã", frutasCategory);
        createItem("Pêra", frutasCategory);
        createItem("Ameixa", frutasCategory);
        createItem("Melancia", frutasCategory);
        createItem("Melão", frutasCategory);
        createItem("Laranja", frutasCategory);
        createItem("Pêssego", frutasCategory);
        createItem("Mirtilos", frutasCategory);
        createItem("Uvas", frutasCategory);
        createItem("Ananás", frutasCategory);
        createItem("Limão", frutasCategory);
        createItem("Romã", frutasCategory);
        createItem("Amoras", frutasCategory);
        createItem("Framboesa", frutasCategory);
        createItem("Kiwi", frutasCategory);
        createItem("Manga", frutasCategory);
        createItem("Maracujá", frutasCategory);
        createItem("Morango", frutasCategory);

        //CEREAIS
        createItem("Aveia",  cereaisCategory);
        createItem("Massa",  cereaisCategory);
        createItem("Cereias Dinis",  cereaisCategory);
        createItem("Cereais Eduardo",  cereaisCategory);
        createItem("Farinha",  cereaisCategory);
        createItem("Farinha Sem gluten",  cereaisCategory);

        //CARNES
        createItem("Perú",  carnesCategory);
        createItem("Porco",  carnesCategory);
        createItem("Frango",  carnesCategory);
        createItem("Picanha",  carnesCategory);
        createItem("Frango",  carnesCategory);

        //PEIXES
        createItem("Salmão",  peixeCategory);
        createItem("Pescada",  peixeCategory);
        createItem("Bacalhau",  peixeCategory);

        //BEBIDAS
        createItem("Água", bebidasCategory);
        createItem("SevenUp", bebidasCategory);
        createItem("Fanta", bebidasCategory);
        createItem("Coca-Cola", bebidasCategory);
        createItem("Sangria", bebidasCategory);
        createItem("Cerveja", bebidasCategory);

        //CONDIMENTOS
        createItem("Curcuma", condimentosCategory);
        createItem("Sal", condimentosCategory);
        createItem("Alho", condimentosCategory);
        createItem("Orégãos", condimentosCategory);

        //LATICINIOS
        createItem("Leite Normal", laticiniosCategory);
        createItem("Leite Sem Lactose", laticiniosCategory);
        createItem("Queijo para fundir", laticiniosCategory);
        createItem("Queijo amanteigado", laticiniosCategory);
        createItem("Queijo Brie", laticiniosCategory);

        //LEGUMES
        createItem("Pepino", legumesCategory);
        createItem("Alface", legumesCategory);
        createItem("Tomate", legumesCategory);
        createItem("Rúcula", legumesCategory);
        createItem("Batata", legumesCategory);
        createItem("Cebola", legumesCategory);
        createItem("Cenoura", legumesCategory);

        //PETISCOS
        createItem("Batatas Fritas", petiscosCategory);
        createItem("Bolachas", petiscosCategory);
        createItem("Bolachas Pepitas", petiscosCategory);
        createItem("Gelados", petiscosCategory);
        createItem("Chocolate", petiscosCategory);

        //Outros
        createItem("Café", outrosCategory);
        createItem("Sacos lixo 30L", outrosCategory);
        createItem("Sacos lixo 50L", outrosCategory);

    }
}
