package com.crackgonzalez.sigmav3.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crackgonzalez.sigmav3.R;
import com.crackgonzalez.sigmav3.modelos.Servicio;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ServicioViewHolder extends RecyclerView.ViewHolder {

    private TextView mTxvSigla;
    private TextView mTxvHis;
    private TextView mTxvHts;
    private TextView mTxvCarga;
    private TextView mTxvRecargo;
    private TextView mTxvPermanencia;
    private TextView mTxvFecha;

    public ServicioViewHolder(View itemView) {
        super(itemView);

        mTxvSigla = itemView.findViewById(R.id.txv_sigla);
        mTxvHis = itemView.findViewById(R.id.txv_his);
        mTxvHts = itemView.findViewById(R.id.txv_hts);
        mTxvCarga = itemView.findViewById(R.id.txv_carga);
        mTxvRecargo = itemView.findViewById(R.id.txv_recargo);
        mTxvPermanencia = itemView.findViewById(R.id.txv_permanencia);
        mTxvFecha = itemView.findViewById(R.id.txv_fecha);
    }

    public void bindToServicio(Servicio servicio) {

        mTxvSigla.setText(servicio.sigla);
        mTxvHis.setText(decimalHora(servicio.his));
        mTxvHts.setText(decimalHora(servicio.hts));
        mTxvCarga.setText(decimalHora(servicio.carga));
        mTxvRecargo.setText(decimalHora(servicio.recargo));
        mTxvPermanencia.setText(decimalHora(servicio.permanencia));
        mTxvFecha.setText(longFecha(servicio.fecha));
    }

    private String longFecha(long fechaLong){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yyyy", Locale.getDefault());
        Date date  = new Date(fechaLong);
        return sdf.format(date);
    }

    private String decimalHora(double horaDecimal){
        String horaString = String.valueOf(horaDecimal);
        String[] partes = horaString.split("\\.");
        int hora = Integer.parseInt(partes[0]);
        int minutos = (int)Math.round((horaDecimal - hora)*60);
        String resultado = "";
        if(minutos>=0 &&minutos<=9){
            resultado = hora +":0"+ minutos;
        }else{
            resultado = hora+":"+minutos;
        }
        return resultado;
    }
}
