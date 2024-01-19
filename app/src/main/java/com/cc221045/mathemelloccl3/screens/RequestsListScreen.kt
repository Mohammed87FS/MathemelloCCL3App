import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
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
    val imagePainter = rememberAsyncImagePainter(model = request.imageUrl)
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

            Spacer(modifier = Modifier.width(16.dp))
// Image
            request.imageUrl?.let { imageUrl ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp), // Define the shape of the card
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Image(
                        painter = imagePainter,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .height(150.dp) // Set a fixed height
                            .fillMaxWidth() // Ensure it fills the width of the card
                            .clip(RoundedCornerShape(8.dp)) // Clip the image to fit the card shape
                            .aspectRatio(1f), // Maintain aspect ratio
                        contentScale = ContentScale.Crop // Crop the image to fit the dimensions
                    )

                    if (imagePainter.state is AsyncImagePainter.State.Error) {
                        val errorState = imagePainter.state as AsyncImagePainter.State.Error
                        val throwable = errorState.result.throwable
                        Log.e("PostItem", "Error loading image: ${throwable.message}")
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Add space between image and text

                }

            Text("Email: ${request.userEmail}", style = MaterialTheme.typography.bodySmall)
        }
    }
}}
