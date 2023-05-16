import SwiftUI
import shared

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            MainView()
        }
    }
}

protocol ListViewListener : AnyObject {
    func clickRoom(room: Km_chat_commonChatRoom)
}

class MainViewModel: ObservableObject, ListViewListener {
    @Published var selectedRoom: Km_chat_commonChatRoom?
    
    var roomId: Int64 {
        selectedRoom?.id ?? -1
    }
    
    var entered: Bool {
        get {
            selectedRoom != nil
        }
        set {
            if(!newValue) {
                selectedRoom = nil
            }
        }
    }
    
    func clickRoom(room: Km_chat_commonChatRoom) {
        selectedRoom = room
    }
    
}

struct MainView: View{
    @StateObject private var viewModel = MainViewModel()
    
    var body: some View {
        ListView(listener: viewModel)
            .sheet(isPresented: $viewModel.entered, content: { ChatView(roomId: viewModel.roomId) })
    }
}

struct ChatView: UIViewControllerRepresentable {
    var roomId: Int64
    
    func makeUIViewController(context: Context) -> UIViewController {
        return ControllersKt.ChatController(roomId: roomId)
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ListView: UIViewControllerRepresentable {
    weak var listener: ListViewListener?
    
    func makeUIViewController(context: Context) -> UIViewController {
        return ControllersKt.ChatRoomListController { room in
            listener?.clickRoom(room:room)
        }
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
