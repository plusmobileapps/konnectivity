//
//  KonnectivityStatusView.swift
//  iosApp
//
//  Created by Andrew Steinmetz on 11/30/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct KonnectivityStatusView: View {
    let bloc: KonnectivityStatusBloc
    
    @ObservedObject
    private var models: ObservableValue<KonnectivityStatusBlocModel>
    
    init(bloc: KonnectivityStatusBloc) {
        self.bloc = bloc
        self.models = ObservableValue(bloc.models)
    }
    
    var body: some View {
        let model = models.value
        
        Text("Current network status: \(model.currentNetworkStatus)")
    }
}
