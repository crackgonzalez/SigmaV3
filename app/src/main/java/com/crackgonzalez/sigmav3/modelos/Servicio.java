package com.crackgonzalez.sigmav3.modelos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Servicio {

    public String sigla;
    public double his;
    public double hts;
    public double carga;
    public long fecha;
    public double recargo;
    public double permanencia;
    public String uid;

    public Servicio() {
    }

    public Servicio(String sigla, double his, double hts, double carga, long fecha, String uid) {
        this.sigla = sigla;
        this.his = his;
        this.hts = hts;
        this.carga = carga;
        this.fecha = fecha;
        this.recargo = calcularRecargo();
        this.permanencia = calcularPermanencia();
        this.uid = uid;
    }

    @Exclude
    public Map<String, Object> servicioMap() {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("sigla", sigla);
        resultado.put("his", his);
        resultado.put("hts", hts);
        resultado.put("carga", carga);
        resultado.put("fecha", fecha);
        resultado.put("recargo", recargo);
        resultado.put("permanencia", permanencia);
        resultado.put("uid", uid);

        return resultado;
    }

    private double calcularPermanencia(){
        double resultado = 0;
        double constante = 24.0;
        if(hts<his){
            resultado = (constante+hts) - his;
        }else{
            resultado = hts - his;
        }
        return Math.round(resultado*100.0)/100.0;
    }

    private double calcularRecargo(){
        final double T1 = 9.0;
        final double T2 = 18.0;
        final double T3 = 0;
        final double veinticuatro = 24.0;
        final double seis = 6.0;
        double resultado = 0;
        if(his !=0 && hts!=0) {
            if (his < T1 && hts < T2) {
                resultado = T1 - his;
            } else if (his < T2 && hts > T2) {
                resultado = hts - T2;
            } else if (his < T2 && hts >= T3) {
                resultado = (hts + veinticuatro) - T2;
            } else if (his > T2 && hts < T1) {
                resultado = his - hts - seis;
            }
        }
        return Math.round(resultado*100.0)/100.0;
    }
}
