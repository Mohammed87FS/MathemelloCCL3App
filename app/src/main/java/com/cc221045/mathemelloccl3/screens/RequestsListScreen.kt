import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cc221045.mathemelloccl3.data.Request
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel


@Composable
fun RequestsListScreen(viewModel: MainViewModel, userEmail: String, isAdmin: Boolean) {
    val userEmail = viewModel.userEmail
    var requests by remember { mutableStateOf(listOf<Request>()) }

    LaunchedEffect(userEmail, isAdmin) {
        requests = if (isAdmin) {
            viewModel.getAllRequests()
        } else {
            viewModel.getUserRequests(userEmail)
        }
    }


    if (requests.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("No requests available", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(requests) { request ->
                RequestItem(request)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun RequestItem(request: Request) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(request.title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(request.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Email: ${request.userEmail}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
