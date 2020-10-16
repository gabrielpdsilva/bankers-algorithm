package controller;

public class DeadlockAlgorithm {
	
	private int totalDeProcessos;
	private int totalDeRecursos;	
	private int posicaoDoProcesso = 0;
	
	private int[][] recursosAlocados;
	private int[][] recursosNecessarios;
	private int[] vezesExecutadasDoProcesso;
	private int[] recursosDisponiveis;
	private int[] recursosExistentes;
	private int[] somatoriaRecursosAlocados;
	private boolean[] processoServido;
	private boolean[] impasse;
	
	public DeadlockAlgorithm(int totalDeProcessos, int totalDeRecursos, int[][] recursosAlocados, int[][] recursosNecessarios, int[] recursosExistentes) {
		
		this.totalDeProcessos = totalDeProcessos;
		this.totalDeRecursos = totalDeRecursos;
		this.recursosAlocados = recursosAlocados;
		this.recursosNecessarios = recursosNecessarios;
		this.recursosExistentes = recursosExistentes;
		
		this.vezesExecutadasDoProcesso = new int[totalDeProcessos];
		this.processoServido = new boolean[totalDeProcessos];
		this.impasse = new boolean[totalDeProcessos];
		
		this.recursosDisponiveis = new int[totalDeRecursos];
		this.somatoriaRecursosAlocados = new int[totalDeRecursos];
		
		//definindo valor padrao pros vetores responsaveis pelos processos
		for(int i = 0; i < totalDeProcessos; i++){
			vezesExecutadasDoProcesso[i] = 0;
			processoServido[i] = false;
			impasse[i] = false;
		}
		
		//definindo valor padrao pros vetores responsaveis pelos recursos
		for(int i = 0; i < totalDeRecursos; i++){
			recursosDisponiveis[i] = 0;
			somatoriaRecursosAlocados[i] = 0;
		}
	}
	
	private int[] pegaQtdRecursosEmUso(int[][] recursosAlocados){
		
		for(int processo = 0; processo < this.totalDeProcessos; processo++){
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
				somatoriaRecursosAlocados[recurso] += recursosAlocados[processo][recurso];
			}
		}
		
		return somatoriaRecursosAlocados;
	}
	
	private boolean recursosValidos(){
		
		//se os recursos existentes nao satisfazerem
		//os recursos necessarios + recursos alocados
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++)

				if(recursosExistentes[recurso] <
				  (recursosNecessarios[processo][recurso] + recursosAlocados[processo][recurso]))
					return false;
		
		return true;
		
	}
	
	private boolean deadlock(){
		
		//se processou mais do que o total de processos e ainda
		//assim nao conseguiu finalizar, deu deadlock
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			if(vezesExecutadasDoProcesso[processo] >= this.totalDeProcessos){
				System.out.println("\nDEADLOCK, motivo: algum processo rodou mais do que o necess�rio e ainda n�o conseguiu\nser executado, ou seja, jamais haver�o recursos suficientes para ele executar.");
				return true;
			}
				
		
		//conferindo se conseguiu rodar algum processo
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			if(!impasse[processo])
				return false;
		
		System.out.println("\nDEADLOCK, motivo: nenhum processo p�de ser executado.");
		return true;
	}
	
	private int[] calcularRecursosDisponiveis(int[] recursosExistentes, int[] somatoriaRecursosAlocados){
		
		for(int recurso = 0; recurso < this.totalDeRecursos; recurso++)
			recursosDisponiveis[recurso] = recursosExistentes[recurso] - somatoriaRecursosAlocados[recurso];
	
		return recursosDisponiveis;
	}
	
	private int[][] criarCopiaDaMatriz(int[][] antigaMatriz){
		int[][] novaMatriz = new int[totalDeProcessos][totalDeRecursos];
		for(int i = 0; i < this.totalDeProcessos; i++){
			for(int j = 0; j < this.totalDeRecursos; j++){
				novaMatriz[i][j] = antigaMatriz[i][j];
			}
		}
		return novaMatriz;
	}
	
	public void usarRecurso(int processo){
		System.out.println("Processo[" + processo + "] est� usando recurso.");
		
		int[][] recursosAlocadosAntesDaSoma = criarCopiaDaMatriz(recursosAlocados);
		
		for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
			
			//se tem recursos pra emprestar
			if(recursosDisponiveis[recurso] > 0){
				
				//recursos disponiveis estao sendo emprestados aos processos
				recursosAlocados[processo][recurso] += recursosDisponiveis[recurso];
				
				//recursos disponiveis diminuiram
				recursosDisponiveis[recurso] -= recursosAlocadosAntesDaSoma[processo][recurso];
			}
			
		}
	}
	
	private void resetarImpasses(){
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			impasse[processo] = false;
	}
	
	private void devolverRecurso(int processo){
		for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
			
			//recursos antes usados agora serao
			//devolvidos para os recursos disponiveis
			recursosDisponiveis[recurso] = recursosAlocados[processo][recurso];

			//zerando a quantidade de recursos
			//que aquele processo precisa
			recursosAlocados[processo][recurso] = 0;
			recursosNecessarios[processo][recurso] = 0;
			
		}
		processoServido[processo] = true;
		System.out.println("Processo[" + processo + "] devolveu o recurso. Ficou em " + (this.posicaoDoProcesso+1) + "o lugar.");
		this.posicaoDoProcesso++;
		
		System.out.println("\n--------------------------------------------------------------");
		System.out.println("Exibindo recursos dispon�veis ap�s o processo devolver recurso:");
		mostrarRecursosDisponiveis();
		
		//serviu o processo, esque�a os antigos
		//impasses e tente novamente, talvez tenha
		//conseguido novos recursos.
		resetarImpasses();
	}
	
	private void mostrarAndamentoDaComparacao(int processo, int recurso){
		System.out.println();
		System.out.println("=============================================");
		System.out.println("Analisando o processo " + processo + ", recurso " + recurso);
		System.out.println("Analisando se recurso dispon�vel (" + recursosDisponiveis[recurso] + ") < recurso necess�rio ("+ recursosNecessarios[processo][recurso] + ")...");
	}
	
	private void mostrarVezesExecutadas(int processo){
		System.out.println("\n*********************************************");
		System.out.println("*Processo[" + processo + "] est� sendo executado pela " + vezesExecutadasDoProcesso[processo] + "x...*");
		System.out.println("*********************************************\n");
	}
	
	private void compararRecursos(int[][] recursosNecessarios, int[] recursosDisponiveis){
		
		for(int processo = 0; processo < this.totalDeProcessos; processo++){
			
			//se ja foi servido, va servir o proximo processo
			if(processoServido[processo])
				continue;
			
			//passou pelo processo
			vezesExecutadasDoProcesso[processo]++;
			
			mostrarVezesExecutadas(processo);
			
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
				
				mostrarAndamentoDaComparacao(processo, recurso);
				
				if(recursosDisponiveis[recurso] < recursosNecessarios[processo][recurso]){
					impasse[processo] = true;
					System.out.println("Processo[" + processo + "] est� inseguro.");
					break;
				}
				
				//chegou ate o fim dos recursos e nao houve impasses
				else if((recurso >= (this.totalDeRecursos-1)) && (!impasse[processo])){
					
					System.out.println("\nO processo " + processo + " � seguro!");
					usarRecurso(processo);
					devolverRecurso(processo);
					
					//for�ando a voltar a comparar desde o primeiro
					//processo, em vez de seguir em frente.
					//Necessario colocar -1 pois se colocar 0, o loop
					//vai come�ar do 1.
					processo = -1;
				}
				
			}
			
		}
	}
	
	private void mostrarRecursosDisponiveis(){
		System.out.println("================");
		System.out.println("Rec. dispon�veis: ");
		for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
			System.out.print(recursosDisponiveis[recurso] + " ");
		}
		System.out.println();
	}
	
	public void mostrarRecursosAlocados(){
		System.out.println("================");
		System.out.println("Rec. alocados: ");
		for(int processo = 0; processo < this.totalDeProcessos; processo++){
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
				System.out.print(recursosAlocados[processo][recurso] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void mostrarRecursosNecessarios(){
		System.out.println("================");
		System.out.println("Rec. necessarios: ");
		for(int processo = 0; processo < this.totalDeProcessos; processo++){
			for(int recurso = 0; recurso < this.totalDeRecursos; recurso++){
				System.out.print(recursosNecessarios[processo][recurso] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
	}
	
	private boolean processosServidos(){
		for(int processo = 0; processo < this.totalDeProcessos; processo++)
			if(!processoServido[processo])
				return false;
		
		return true;
	}
	
	//TODO
	public void realizarAnalise(){
		this.somatoriaRecursosAlocados = pegaQtdRecursosEmUso(recursosAlocados);
		this.recursosDisponiveis = calcularRecursosDisponiveis(recursosExistentes, somatoriaRecursosAlocados);
		
		//if(!recursosValidos()){
		//	System.out.println("Total de recursos existentes < (total de recursos alocados + total de recursos necess�rios), imposs�vel prosseguir.");
		//	return;
		//}
		//recursosDisponiveis[0] = 1;
		//recursosDisponiveis[1] = 1;
		//recursosDisponiveis[2] = 0;
		//recursosDisponiveis[3] = 2;
		
		mostrarRecursosAlocados();
		mostrarRecursosNecessarios();
		mostrarRecursosDisponiveis();
		
		while(!processosServidos() && !deadlock()){
			compararRecursos(recursosNecessarios, recursosDisponiveis);
		}
		
		System.out.println();
		System.out.println("AN�LISE FINALIZADA.");
		//int cont = 1;
		//while(cont < 40){
		//	compararRecursos(recursosNecessarios, recursosDisponiveis);
		//	cont++;
	}

}
