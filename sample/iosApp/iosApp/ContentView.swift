import SwiftUI
import shared

struct ContentView: View {
    
    @State
    private var blocHolder = BlocHolder {
        KonnectivityStatusBlocKt.KonnectivityStatusBloc(context: $0)
    }

	var body: some View {
        KonnectivityStatusView(bloc: blocHolder.bloc)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
