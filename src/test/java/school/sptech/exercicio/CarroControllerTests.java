package school.sptech.exercicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Field;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class CarroControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarroController carroController;

    @Nested
    public class Post {

        @BeforeEach
        public void setUp() {
            carroController = new CarroController();
            mockMvc = MockMvcBuilders.standaloneSetup(carroController).build();
        }

        @Nested
        public class CadastroComValoresUnicos {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {

                String carroJson = "{ \"marca\": \"Volkswagen\", \"modelo\": \"Golf\", \"cor\": \"Branco\", \"placa\": \"BRA2E19\", \"preco\": 35000.00, \"ano\": 2019 }";

                mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJson))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.placa").value("BRA2E19"))
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.id").value(1));
            }
        }

        @Nested
        @DisplayName("Cadastro com erro - Placa inválida")
        public class CadastroComPlacaInvalida {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {

                String carroJson = "{ \"marca\": \"Toyota\", \"modelo\": \"Corolla\", \"cor\": \"Preto\", \"placa\": \"ABC1D2w\", \"preco\": 45000.00, \"ano\": 2018 }";

                mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJson))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("Cadastro com erro - Placa duplicada")
        public class CadastroComPlacaDuplicada {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {
                String carroJson = "{\"marca\":\"Toyota\",\"modelo\":\"Corolla\",\"ano\":2020,\"cor\":\"Preto\",\"placa\":\"BRA2E19\"}";

                mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJson))
                        .andExpect(status().isCreated());

                mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJson))
                        .andExpect(status().isConflict());
            }

        }

        @Nested
        @DisplayName("ID nao deve ser reaproveitado")
        public class IdNaoDeveSerReaproveitado {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {
                String carroJson = "{\"marca\":\"Toyota\",\"modelo\":\"Corolla\",\"ano\":2020,\"cor\":\"Preto\",\"placa\":\"BRA2E19\"}";

                mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJson))
                        .andExpect(status().isCreated());

                mockMvc.perform(delete("/carros/1"))
                        .andExpect(status().isNoContent());

                mockMvc.perform(post("/carros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJson))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").value(2));
            }
        }
    }

    @Nested
    public class Get {

        @BeforeEach
        public void setUp() {
            carroController = new CarroController();
            mockMvc = MockMvcBuilders.standaloneSetup(carroController).build();
        }

        @Nested
        @DisplayName("Listagem de carros")
        public class Listagem {

            @Test
            @DisplayName("Deve retornar 204 - Lista vazia")
            public void deveRetornar_204() throws Exception {

                mockMvc.perform(get("/carros"))
                        .andExpect(status().isNoContent());
            }

            @Test
            @DisplayName("deve retornar 200 - Lista com 3 carros")
            public void deveRetornar_200() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));

                mockMvc.perform(get("/carros"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].placa").value("BRA2E19"))
                        .andExpect(jsonPath("$[1].placa").value("BRA2E20"))
                        .andExpect(jsonPath("$[2].placa").value("BRA2E21"));
            }
        }

        @Nested
        @DisplayName("Busca por id")
        public class BuscaPorId {

            @Test
            @DisplayName("ID existente")
            void comIdExistente() throws Exception{
                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));

                mockMvc.perform(get("/carros/2"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(2));
            }


            @Test
            @DisplayName("lista vazia")
            public void comListaVazia() throws Exception {
                mockMvc.perform(get("/carros/1"))
                        .andExpect(status().isNotFound());
            }

            @Test
            @DisplayName("lista cheia, id inexistente")
            public void comListaCheia() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));

                mockMvc.perform(get("/carros/4"))
                        .andExpect(status().isNotFound());
            }
        }

        @Nested
        @DisplayName("Valor médio por marca")
        public class ValorMedioPorMarca {

            @Test
            @DisplayName("Marca inexistente")
            public void marcaInexistente() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));
                carros.add((Carro) CarroTeste.getInstanceForQuery(4, "Fiat", "Palio", "Vermelho", "BRA2E22", 30000.00,
                        2017));
                carros.add((Carro) CarroTeste.getInstanceForQuery(5, "Peugeot", "208", "Preto", "BRA2E23", 40000.00,
                        2017));

                mockMvc.perform(get("/carros/valor-medio?marca=Renault"))
                        .andExpect(status().isNoContent());
            }

            @Test
            @DisplayName("Marca existente")
            public void marcaExistente() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));
                carros.add((Carro) CarroTeste.getInstanceForQuery(4, "Fiat", "Palio", "Vermelho", "BRA2E22", 30000.00,
                        2017));
                carros.add((Carro) CarroTeste.getInstanceForQuery(5, "Peugeot", "208", "Preto", "BRA2E23", 40000.00,
                        2017));

                mockMvc.perform(get("/carros/valor-medio?marca=Fiat"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("27500.0"));
            }

            @Test
            @DisplayName("Marca existente - Case insensitive")
            public void marcaExistenteCaseInsensitive() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));
                carros.add((Carro) CarroTeste.getInstanceForQuery(4, "Fiat", "Palio", "Vermelho", "BRA2E22", 30000.00,
                        2017));
                carros.add((Carro) CarroTeste.getInstanceForQuery(5, "Peugeot", "208", "Preto", "BRA2E23", 40000.00,
                        2017));

                mockMvc.perform(get("/carros/valor-medio?marca=fiat"))
                        .andExpect(status().isOk())
                        .andExpect(content().string("27500.0"));
            }
        }
    }

    @Nested
    public class Put {

        @BeforeEach
        public void setUp() {
            carroController = new CarroController();
            mockMvc = MockMvcBuilders.standaloneSetup(carroController).build();
        }

        @Nested
        @DisplayName("Atualização correta")
        public class AtualizacaoCorreta {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));

                String carroJsonAtualizado = "{\"marca\":\"Volkswagen\",\"modelo\":\"Golf\",\"ano\":2020,\"cor\":\"Branco\",\"placa\":\"BRA2E30\",\"preco\":45000.00}";

                mockMvc.perform(put("/carros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJsonAtualizado))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.preco").value(45000.00));
            }
        }

        @Nested
        @DisplayName("Atualização com erro - Placa duplicada")
        public class AtualizacaoIncorretaPlacaDuplicada {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));

                String carroJson = "{\"marca\":\"Toyota\",\"modelo\":\"Corolla\",\"ano\":2020,\"cor\":\"Preto\",\"placa\":\"BRA2E19\",\"preco\":45000.00}";

                mockMvc.perform(put("/carros/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJson))
                        .andExpect(status().isConflict());
            }
        }

        @Nested
        @DisplayName("Atualização com erro - Placa inválida")
        public class AtualizacaoIncorretaPlacaInvalida {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));

                String carroJson = "{\"marca\":\"Toyota\",\"modelo\":\"Corolla\",\"ano\":2020,\"cor\":\"Preto\",\"placa\":\"ABC1D2w\",\"preco\":45000.00}";

                mockMvc.perform(put("/carros/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(carroJson))
                        .andExpect(status().isBadRequest());

            }
        }
    }

    @Nested
    public class Delete {

        @BeforeEach
        public void setUp() {
            carroController = new CarroController();
            mockMvc = MockMvcBuilders.standaloneSetup(carroController).build();
        }

        @Nested
        @DisplayName("Exclusão correta")
        public class ExclusaoCorreta {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));
                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Toyota", "Corolla", "Preto", "BRA2E20", 45000.00,
                        2018));
                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Uno", "Vermelho", "BRA2E21", 25000.00,
                        2017));

                mockMvc.perform(delete("/carros/1"))
                        .andExpect(status().isNoContent());
            }
        }

        @Nested
        @DisplayName("Exclusão incorreta")
        public class ExclusaoIncorreta {

            @Test
            @DisplayName("ID inexistente")
            public void comIdInexistente() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));

                mockMvc.perform(delete("/carros/2"))
                        .andExpect(status().isNotFound());
            }

            @Test
            @DisplayName("Lista vazia")
            public void comListaVazia() throws Exception {

                mockMvc.perform(delete("/carros/1"))
                        .andExpect(status().isNotFound());
            }
        }
    }

    @Nested
    public class Patch {

        @BeforeEach
        public void setUp() {
            carroController = new CarroController();
            mockMvc = MockMvcBuilders.standaloneSetup(carroController).build();
        }

        @Nested
        @DisplayName("Atualização parcial correta")
        public class AtualizacaoParcialCorreta {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");
                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Gol", "Branco", "BRA2E19",
                        35000.00, 2019));

                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));

                mockMvc.perform(patch("/carros/1/emplacamento/BRA2E30")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.placa").value("BRA2E30"));
            }
        }

        @Nested
        @DisplayName("Atualização parcial com erro - Placa duplicada")
        public class AtualizacaoParcialIncorretaPlacaDuplicada {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");

                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Gol", "Branco", "BRA2E19",
                        35000.00, 2019));

                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));

                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Palio", "Vermelho", "BRA2E21",
                        25000.00, 2017));

                mockMvc.perform(patch("/carros/2/emplacamento/BRA2E21")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isConflict());
            }
        }

        @Nested
        @DisplayName("Atualização parcial com erro - Placa inválida")
        public class AtualizacaoParcialIncorretaPlacaInvalida {

            @Test
            @DisplayName("Cenário único")
            public void cenarioUnico() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");

                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Gol", "Branco", "BRA2E19",
                        35000.00, 2019));

                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));

                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Palio", "Vermelho", "BRA2E21",
                        25000.00, 2017));

                mockMvc.perform(patch("/carros/2/emplacamento/ABC1D2w")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("Atualização parcial com erro - ID inexistente")

        public class AtualizacaoParcialIncorretaIdInexistente {

            @Test
            @DisplayName("ID inexistente")
            public void comIdInexistente() throws Exception {

                Field carrosField = CarroController.class.getDeclaredField("carros");

                carrosField.setAccessible(true);

                List<Carro> carros = (List<Carro>) carrosField.get(carroController);

                carros.clear();

                carros.add((Carro) CarroTeste.getInstanceForQuery(1, "Volkswagen", "Gol", "Branco", "BRA2E19",
                        35000.00, 2019));

                carros.add((Carro) CarroTeste.getInstanceForQuery(2, "Volkswagen", "Golf", "Branco", "BRA2E19",
                        35000.00, 2019));

                carros.add((Carro) CarroTeste.getInstanceForQuery(3, "Fiat", "Palio", "Vermelho", "BRA2E21",
                        25000.00, 2017));

                mockMvc.perform(patch("/carros/4/emplacamento/BRA2E30")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }

            @Test
            @DisplayName("Lista vazia")
            public void comListaVazia() throws Exception {

                mockMvc.perform(patch("/carros/1/emplacamento/BRA2E30")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            }
        }
    }
}
