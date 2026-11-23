package busServices;

import java.util.Calendar;

public class Viagem {

	private String origem, destino;
    private Calendar dataViagem;
    private double precoLeito, precoPadrão;
    private Onibus onib; //instância de ônibus que fará a viagem
	
    public Viagem(String origem, String destino, Calendar dataViagem,
			double precoLeito, double precoPadrão, Onibus on) {
		super();
		this.origem = origem;
		this.destino = destino;
		this.dataViagem = dataViagem;
		this.precoLeito = precoLeito;
		this.precoPadrão = precoPadrão;
		this.onib = on;
	}
    
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Calendar getDataViagem() {
		return dataViagem;
	}
	public void setDataViagem(Calendar dataViagem) {
		this.dataViagem = dataViagem;
	}
	public double getPrecoLeito() {
		return precoLeito;
	}

	
	public double getPrecoPassagem(TipoDePoltrona tp) {
		
		switch (tp) {
			case PADRÃO : return this.precoPadrão;
					
			case LEITO  : return this.precoLeito;
				
		}
		return 0;
	
	}
	
	public void setPrecoPassagem(double preco, TipoDePoltrona tp) {
		switch (tp) {
			case PADRÃO : this.precoPadrão = preco;
					
			case LEITO  : this.precoLeito = preco;
		
		}
	}
	
	public Onibus getOnibus() {
		return onib;
	}
	public void setOnibus(Onibus on) {
		this.onib = on;
	}
    
	//calcula total fatura na viagem
    public double calcularTotalFaturado(){
    	double faturaLeito = onib.getCapacidade(TipoDePoltrona.LEITO) - onib.getLugaresLivres(TipoDePoltrona.LEITO) * this.getPrecoPassagem(TipoDePoltrona.LEITO);
    	double faturaComum = onib.getCapacidade(TipoDePoltrona.PADRÃO) - onib.getLugaresLivres(TipoDePoltrona.PADRÃO) * this.getPrecoPassagem(TipoDePoltrona.LEITO);
        return faturaLeito + faturaComum;
    }
    
    //caclula taxa de Ocupação Global do Onibus
    public double getTaxaOcupação()
    {
    	return (double)  (onib.getCapacidade() - onib.getLugaresLivres()) / onib.getCapacidade();
    }
    
    //sobrecrga calcula taxa de Ocupação do Onibus por TipoDePoltrona
    public double getTaxaOcupação(TipoDePoltrona tp)
    {
    	return (double) onib.getCapacidade(tp) - onib.getLugaresLivres(tp) / onib.getCapacidade(tp);
    }
}
