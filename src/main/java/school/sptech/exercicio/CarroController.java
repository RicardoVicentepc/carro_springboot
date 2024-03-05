package school.sptech.exercicio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/carros")
public class CarroController {
    List<Carro> carros = new ArrayList<>();

    @GetMapping
    public List<Carro> exibir(){
        return this.carros;
    }
    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody Carro carro){
        for (Carro carroAtual : carros){
            if(carroAtual.getPlaca().equals(carro.getPlaca())){return ResponseEntity.status(404).build();}
        }

        if(placaValida(carro.getPlaca())){ carros.add(carro); }

        return ResponseEntity.status(200).body("Carro cadastrado com a placa = " + carro.getPlaca());
    }

    @GetMapping("/{placa}")
    public Carro buscarCarro(@PathVariable String placa){
        for (Carro carro : carros){
            if(carro.getPlaca().equals(placa)){return carro;}
        }
        return null;
    }

    @PutMapping("/{indice}")
    public ResponseEntity<Carro> atualizar(@PathVariable int indice, @RequestBody Carro carro){
        if(placaValida(carro.getPlaca())) {
            this.carros.set(indice, carro);

            return ResponseEntity.status(200).body(carro);
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{indice}")
    public ResponseEntity<Carro> deletar(@PathVariable int indice){
        this.carros.remove(indice);
        return ResponseEntity.status(200).build();
    }

    @PatchMapping("/{indice}/emplacamento/{placa}")
    public ResponseEntity<Carro> atualizaPlaca(@PathVariable int indice, @PathVariable String placa){
        if(placaValida(placa)){
            carros.get(indice).setPlaca(placa);
            return ResponseEntity.status(200).body(carros.get(indice));
        }
        return ResponseEntity.status(404).build();
    }
    @GetMapping("/valor-medio/")
    public ResponseEntity<String> valorMedioPorMarca(@RequestParam String marca){
        int aux = 0;
        double total = 0;
        for (Carro carro : this.carros){
            if(carro.getMarca().equals(marca)) { total += carro.getPreco(); aux++;}
        }
        System.out.println("TOTAL = " + total);
        System.out.println("AUX = " + aux);
        double media = total / aux;
        return ResponseEntity.status(200).body( String.format(" Media %s", media));
    }

    public Boolean placaValida(String placa){
        return placa.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$") ? true : false;
    }


}
