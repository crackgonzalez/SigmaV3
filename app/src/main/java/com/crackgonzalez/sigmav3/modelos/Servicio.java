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
        double resultado = 0;
        if(his > 18.0 && his < 24.0){
            //noche
            if(hts >his && hts < 24.0){
                resultado = hts - his;
            }else if(hts>= 0 && hts < 9.0){
                resultado = 24.0 - his + hts;
            }
        }else if(his >= 0 && his < 9.0){
            //mañana
            if(hts > his ){
                if(hts < 9.0){
                    resultado = hts - his;
                }else if(hts > 9.0 && hts < 18.0){
                    resultado = 9.0 - his;
                }else if(hts > 18.0 && hts < 24.0){
                    resultado = (9.0 - his) + (hts - 18.0);
                }
            }
        }
        else if(his >= 9.0 && his<= 18.0){
            if(hts>9.0 && hts <= 18.0){
                resultado = 0;
            }else if(hts > 18.0 && hts < 24.0){
                resultado = hts - 18.0;
            }else if(hts > 0 && hts < 9.0){
                resultado = 24.0 - 18.0 + hts;
            }

        }

        return Math.round(resultado*100.0)/100.0;
    }
}
