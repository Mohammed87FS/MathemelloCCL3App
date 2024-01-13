
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cc221045.mathemelloccl3.data.Request
import com.cc221045.mathemelloccl3.ui.theme.MainViewModel



@Composable
fun RequestsListScreen(viewModel: MainViewModel, userEmail: String, isAdmin: Boolean) {
    val requests = if (isAdmin) {
        viewModel.getAllRequests().observeAsState(initial = listOf()).value
    } else {
        viewModel.getUserRequests(userEmail).observeAsState(initial = listOf()).value
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(requests) { request ->
            RequestItem(request)
            Spacer(modifier = Modifier.height(8.dp))
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

        }
    }
}
