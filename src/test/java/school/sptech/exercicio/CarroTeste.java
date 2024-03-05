package school.sptech.exercicio;

import java.lang.reflect.Field;

public class CarroTeste {

    public static Object getInstanceForQuery(
            int id, String marca, String modelo, String cor, String placa, double preco, int ano) {

        try {
            Class<?> clazz = Class.forName("school.sptech.exercicio.Carro");
            Object carro = clazz.getDeclaredConstructor().newInstance();

            setField(carro, "id", id);
            setField(carro, "marca", marca);
            setField(carro, "modelo", modelo);
            setField(carro, "cor", cor);
            setField(carro, "placa", placa);
            setField(carro, "preco", preco);
            setField(carro, "ano", ano);

            return carro;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void setField(Object object, String fieldName, Object value) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

}
